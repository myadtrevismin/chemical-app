package com.core.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.core.auth.AuthStatConstats;
import com.core.auth.dao.UserDao;
import com.core.auth.trans.UserSessionStore;
import com.core.common.Ack;
import com.core.common.CommonUtils;
import com.core.common.Constants;
import com.core.controller.InventoryDetails;
import com.core.event.service.EventService;
import com.core.mail.MailService;
import com.core.notification.Notification;
import com.core.notification.NotificationService;
import com.core.pdf.PdfLoader;
import com.core.pdf.PdfLoader.Pdfs;
import com.core.user.entity.User;
import com.core.user.repo.UserRepo;
import com.org.order.Order;
import com.org.order.repo.OrderRepo;
import com.org.product.Product;
import com.org.product.ProductFlow;
import com.org.product.repo.ProductFlowRepo;
import com.org.purchase.Purchase;
import com.org.purchase.PurchaseProduct;
import com.org.purchase.projection.PurchaseProductRepo;
import com.org.purchase.repo.PurchaseRepo;
import com.org.sales.Sales;
import com.org.sales.SalesProduct;
import com.org.sales.SalesQuotation;
import com.org.sales.repo.SalesProductRepo;
import com.org.sales.repo.SalesQuotationRepo;

@Service
public class AdminService {
	@Autowired
	UserDao userDao;

	@Autowired
	NotificationService notificationService;

	@Autowired
	EntityManager em;
	@Autowired
	UserRepo userRepo;

	@Autowired
	MailService mailService;

	@Autowired
	PdfLoader pdfLoader;

	@Autowired
	OrderRepo orderRepo;

	@Autowired
	SalesQuotationRepo quoteRepo;
	
	@Autowired
	PurchaseRepo purchaseRepo;
	
	@Autowired
	PurchaseProductRepo purchaseProdRepo;

	@Autowired
	SalesProductRepo prodRepo;

	@Autowired
	ProductFlowRepo productFlowRepo;
	
	@Autowired
	EventService eventService;

	@Transactional
	public void saveUser(User user) {
		String password = user.getPassword();

		if (!CommonUtils.isNotNullOrEmpty(password)) {
			password = CommonUtils.getRandomAlphaNumericString(8);
			user.setPassword(password);
		}

		user.setEnabled(true);
		userDao.createUser(user);

		mailService.sendWelcomeMail(user, password);
	}

	public Ack patchUser(long id) {
		Ack ack = new Ack();

		Optional<User> optional = userRepo.findById(id);

		if (optional.isPresent()) {
			User user = optional.get();

			user.setEnabled(!user.isEnabled());
			userRepo.save(user);

			UserSessionStore uss = AuthStatConstats.getUserSessionStore(user.getId());

			if (uss != null) {
				AuthStatConstats.removeUserSessionStore(user.getId());
			}
		} else {
			ack.setStatus(HttpStatus.BAD_REQUEST.value());
		}

		return ack;
	}

	public Order getInvoice(long id, OutputStream stream) {
		Object obj = null;

//		UserSessionStore uss = CommonUtils.getUserSessionStore();

		Optional<Order> opt = orderRepo.findById(id);

		Order sq = opt.get();

		obj = sq;

		pdfLoader.loadPdf(obj, Pdfs.Invoice, stream);

		return sq;
	}

	public void downloadInvoice(long id, HttpServletResponse response) throws IOException {
		getInvoice(id, response.getOutputStream());
	}

	public void sendInvoice(long id) {
		ByteArrayOutputStream os = new ByteArrayOutputStream();

		Order sq = getInvoice(id, os);

		mailService.sendInvoiceEmail(os, sq, CommonUtils.getUserFromSession());
	}

	public void sendQuotation(long id, long pid) {
		Optional<SalesQuotation> opt = quoteRepo.findById(id);

		SalesQuotation quote = opt.get();
		Sales enqury=quote.getEnquiry();
		Set<SalesProduct> prods = enqury.getProducts();
		SalesProduct product = prods.stream().filter(g -> g.getId() == pid).findFirst().get();

		mailService.sendQuotationEmail(quote, product, CommonUtils.getUserFromSession());

		product.setStatus("Email Sent");
		prodRepo.save(product);

		Notification notification = new Notification();
		notification.setUid(Constants.adminId);
		notification.setType("NEW_FOLLOWUP");
		notification.setDescription("Email quote sent to " + quote.getCompany().getName() + " by "
				+ CommonUtils.getUserFromSession().getName());

		notification.setUrl("/sales/" + quote.getEnquiry().getId());
		notificationService.saveNotification(notification);

	}

	public List<InventoryDetails> loadInventory(Long prodId, Long salesId) {

		List<ProductFlow> flows = productFlowRepo.findByReferenceAndRefType(salesId, "Sales");
		List<Long> parentIds = flows.stream().map(g -> g.getParent()).collect(Collectors.toList());
		List<ProductFlow> batches = em
				.createQuery("select t from ProductFlow t where t.product.id=:prodId and " + " remainingQuantity>0 ",
						ProductFlow.class)
				.setParameter("prodId", prodId).getResultList();

		if (parentIds.size() > 0) {
			List<ProductFlow> addedFLows = productFlowRepo.findAllById(parentIds);
			Set<Long> batchFlows = batches.stream().map(l -> l.getId()).collect(Collectors.toSet());
			addedFLows.stream().filter(p -> !batchFlows.contains(p.getId())).forEach(g -> {
				batches.add(g);
			});
		}

		return batches.stream().map(g -> {

			InventoryDetails id = new InventoryDetails();
			id.setBatch(g.getBatch());
			id.setParent(g.getId());
			id.setAvailableQty(g.getRemainingQuantity());
			Optional<ProductFlow> flowOpt =flows.stream().filter(l -> l.getParent() == g.getId()) 
			.findFirst();
			if(flowOpt.isPresent()) {
				 id.setId(flowOpt.get().getId());
				id.setCurrentQty(flowOpt.get().getQuantity());
			}else {
				id.setCurrentQty(0);
			}
			
			return id;
		}).collect(Collectors.toList());

	}

	@Transactional
	public Ack saveInventory(List<InventoryDetails> inventoryDetails, Long prodId, Long salesId) {
		Set<Long> parentIds = inventoryDetails.stream().map(g -> g.getParent()).collect(Collectors.toSet());
		List<ProductFlow> addedFLows = new ArrayList<>();
		if (parentIds.size() > 0) {
			addedFLows = productFlowRepo.findAllById(parentIds);

		}
		List<ProductFlow> parents=addedFLows;
		List<ProductFlow> saveFlows=new ArrayList<>();
		inventoryDetails.stream().forEach(g -> {
			if (g.getId() != null) {
				Optional<ProductFlow> opt = productFlowRepo.findById(g.getId());
				if (opt.isPresent()) {
					ProductFlow saledFlow=opt.get();
					ProductFlow pf = parents.stream().filter(f -> f.getId() == g.getParent()).findFirst().get();
					pf.setRemainingQuantity((int)(pf.getRemainingQuantity()+ saledFlow.getQuantity()-g.getCurrentQty()));
					saledFlow.setQuantity((int)g.getCurrentQty());
					saveFlows.add(pf);
					saveFlows.add(saledFlow);
					long updateQty=saledFlow.getQuantity()-g.getCurrentQty();
					eventService.updateProductQty(updateQty<0?"Outgoing":"InComing",  saledFlow.getQuantity()-g.getCurrentQty(), prodId);
				}
			}else if(g.getCurrentQty()>0) {
				ProductFlow pf = parents.stream().filter(f -> f.getId() == g.getParent()).findFirst().get();
				pf.setRemainingQuantity((int)(pf.getRemainingQuantity()-g.getCurrentQty()));
				
				ProductFlow saleFlow=new ProductFlow();
				saleFlow.setBatch(pf.getBatch());
				saleFlow.setParent(pf.getParent());
				saleFlow.setQuantity((int)g.getCurrentQty());
				Product p=new Product();
				p.setId(prodId);
				saleFlow.setProduct(p);
				saleFlow.setMfgDate(pf.getMfgDate());
				saleFlow.setExpiryDate(pf.getExpiryDate());
				saleFlow.setOrderProduct(salesId);
				saleFlow.setRefType("Sales");
				saleFlow.setReference(salesId);
				saleFlow.setShelfLife(pf.getShelfLife());
				saleFlow.setType("Outgoing");
				saleFlow.setParent(pf.getId());
				saveFlows.add(pf);
				saveFlows.add(saleFlow);
				eventService.updateProductQty("Outgoing",  g.getCurrentQty(), prodId);
			}
		});
		productFlowRepo.saveAll(saveFlows);
		return new Ack("Success",200);
	}

	public void sendPurchaseEnquiry(long id, long pid) {

		Purchase enqury=purchaseRepo.findById(id).get();
		Set<PurchaseProduct> prods = enqury.getProducts();
		PurchaseProduct product = prods.stream().filter(g -> g.getId() == pid).findFirst().get();

		mailService.sendEnquiryEmail(enqury, product, CommonUtils.getUserFromSession());

		product.setStatus("Email Sent");
		purchaseProdRepo.save(product);

		Notification notification = new Notification();
		notification.setUid(Constants.adminId);
		notification.setType("NEW_FOLLOWUP");
		notification.setDescription("Email enquiry sent to " + enqury.getCompany().getName() + " by "
				+ CommonUtils.getUserFromSession().getName());

		notification.setUrl("/purchase/" + enqury.getId());
		notificationService.saveNotification(notification);
		
	}

}
