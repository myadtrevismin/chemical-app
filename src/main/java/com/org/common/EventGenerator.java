package com.org.common;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.core.auth.dao.UserDao;
import com.core.common.CommonData.TypeRepoEnum;
import com.core.common.CommonUtils;
import com.core.common.Constants;
import com.core.document.service.Document;
import com.core.mail.MailService;
import com.core.notification.Notification;
import com.core.notification.NotificationService;
import com.core.user.entity.User;
import com.org.company.Company;
import com.org.company.repo.CompanyRepo;
import com.org.followup.Followup;
import com.org.order.Accounts;
import com.org.order.Order;
import com.org.order.repo.OrderRepo;
import com.org.product.Product;
import com.org.purchase.Purchase;
import com.org.purchase.repo.PurchaseRepo;
import com.org.sales.Sales;
import com.org.sales.repo.SalesRepo;

@Service
public class EventGenerator {

	@Autowired
	EntityManager em;

	@Autowired
	UserDao userDao;

	@Autowired
	NotificationService notificationService;
	
	@Autowired
	CompanyRepo companyRepo;
	
	@Autowired
	SalesRepo salesRepo;

	@Autowired
	PurchaseRepo purchaseRepo;
	
	@Autowired
	OrderRepo orderRepo;

	
	
	@Autowired
	MailService mailService;
	Logger logger = LoggerFactory.getLogger(EventGenerator.class);

	// every day 6AM..
	@Scheduled(cron = "0 0 6 1/1 * ?")
	public void checkForProductExpiry() {
		ZoneId defaultZoneId = ZoneId.systemDefault();

		List<Product> products = em
				.createQuery("select t from Product t where t.expDate <=:expiryDate and t.expDate >= :fromDate",
						Product.class)
				.setParameter("expiryDate",
						Date.from(LocalDate.now().plusDays(5).atStartOfDay(defaultZoneId).toInstant()))
				.setParameter("fromDate",
						Date.from(LocalDate.now().minusDays(30).atStartOfDay(defaultZoneId).toInstant()))
				.getResultList();

		if (products.size() > 0) {
			logger.info(" Expired products");
			mailService.sendProductExpirty(products);
		} else {
			logger.info("No Expired products");
		}
	}

	@Scheduled(cron = "0 0 6 1/1 * ?")
	//@Scheduled(cron = "0 0/2 * 1/1 * ?")
	@Transactional
	public void checkForDocumentExpiry() {
		ZoneId defaultZoneId = ZoneId.systemDefault();

		List<Document> products = em
				.createQuery(
						"select t from Document t where t.expiryDate is not null and t.expiryDate <=:expiryDate and t.expiryDate >= :fromDate and t.active=true ",
						Document.class)
				.setParameter("expiryDate",
						Date.from(LocalDate.now().plusDays(31).atStartOfDay(defaultZoneId).toInstant()))
				.setParameter("fromDate",
						Date.from(LocalDate.now().plusDays(30).atStartOfDay(defaultZoneId).toInstant()))
				.getResultList();
		
		
		products.addAll(em
		.createQuery(
				"select t from Document t where t.expiryDate is not null and t.expiryDate <=:expiryDate and t.expiryDate >= :fromDate and t.active=true ",
				Document.class)
		.setParameter("expiryDate",
				Date.from(LocalDate.now().minusDays(0).atStartOfDay(defaultZoneId).toInstant()))
		.setParameter("fromDate",
				Date.from(LocalDate.now().minusDays(1).atStartOfDay(defaultZoneId).toInstant()))
		.getResultList());
		
		if (products.size() > 0) {
			logger.info(" Expired documents");
			mailService.sendDocumentExpirty(products);
			
			for(Document post:products) {
				
				TypeRepoEnum repo=TypeRepoEnum.valueOf(post.getFileFrom().split("_")[0]);
				String desc=null;
				String url=null;
				
				SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-YYYY");
				String dateStr=sdf.format(post.getExpiryDate());
				
				if (TypeRepoEnum.companies == repo) {
					Optional<Company> sopt = companyRepo.findById(post.getParent());
					if (sopt.isPresent()) {
						desc = sopt.get().getName()+" " + post.getFileType() + " about to expire on " + dateStr;
						url="/companies/"+post.getParent();
						
						mailService.sendDocumentExpirtyForCompany(post,sopt.get().getName(),sopt.get().getEmail());
						
					}
				} /*
					 * if (TypeRepoEnum.sales == repo) { Optional<Sales> sopt =
					 * salesRepo.findById(post.getParent()); if (sopt.isPresent()) { desc =
					 * "Follow up added to " + post.getRepository() + " order " +
					 * sopt.get().getCode() + " by " + CommonUtils.getUserFromSession().getName();
					 * url="/sales/"+post.getParent(); } } else if (TypeRepoEnum.purchases == repo)
					 * { Optional<Purchase> sopt = purchaseRepo.findById(post.getParent()); if
					 * (sopt.isPresent()) { desc = "Follow up added to " + post.getRepository() +
					 * " order " + sopt.get().getCode() + " by " +
					 * CommonUtils.getUserFromSession().getName();
					 * url="/purchases/"+post.getParent(); } } else if (TypeRepoEnum.orders == repo)
					 * { Optional<Order> sopt = orderRepo.findById(post.getParent()); if
					 * (sopt.isPresent()) { desc = "Follow up added to " + post.getRepository() +
					 * " order " + sopt.get().getCode() + " by " +
					 * CommonUtils.getUserFromSession().getName(); url="/orders/"+post.getParent();
					 * } }
					 */
				

				Notification notification = new Notification();
				notification.setUid(Constants.adminId);
				notification.setType("DOCUMENT_EXPIRED");
				notification.setDescription(desc);
				notification.setUrl(url);
				notification.setCreatedBy(Constants.adminId);
				notification.setUpdatedBy(Constants.adminId);
				notificationService.saveNotification(notification);
			}
			
			
		} else {
			logger.info("No Expired documents");
		}

	}

	@Scheduled(cron = "0 0 7 1/1 * ?")
	public void reminders() {
		ZoneId defaultZoneId = ZoneId.systemDefault();

		List<Followup> followups = em.createQuery(
				"select t from Followup t where t.nextFollowUpDate <=:expiryDate and t.nextFollowUpDate >= :fromDate  ",
				Followup.class)
				.setParameter("expiryDate",
						Date.from(LocalDate.now().plusDays(1).atStartOfDay(defaultZoneId).toInstant()))
				.setParameter("fromDate", Date.from(LocalDate.now().atStartOfDay(defaultZoneId).toInstant()))
				.getResultList();

		Map<Long, List<Followup>> maps = followups.stream().collect(Collectors.groupingBy(Followup::getCreatedBy));
		List<MailObj> adminList = new ArrayList<>();
		maps.forEach((uid, lis) -> {
			User user = userDao.getUser(uid);
			List<MailObj> fmoLis = new ArrayList<>();
			lis.forEach(g -> {
				MailObj fmo = new MailObj();
				fmo.setName(user.getName());
				fmo.setType(g.getNextFollowupType());
				fmo.setContact(g.getContact());
				if (g.getResponse() != null) {
					int max = g.getResponse().length() > 100 ? 100 : g.getResponse().length();
					fmo.setSummary(g.getResponse().substring(0, max));
				}
				fmoLis.add(fmo);
				adminList.add(fmo);
			});
			mailService.sendFollowupsMail(user, fmoLis);

		});

		if (adminList.size() > 0) {
			logger.info(" Followups");
			mailService.sendAdminFollowupsMail(adminList);
		} else {
			logger.info("NO followups");
		}
	}

	// @Scheduled(cron = "0 0/15 * 1/1 * ?")
	public void paymentAlerts() {
		ZoneId defaultZoneId = ZoneId.systemDefault();

		List<Accounts> followups = em
				.createQuery("select t from Accounts t where t.dueDate <=:expiryDate and"
						+ " t.dueDate >= :fromDate  and t.status='On Going'", Accounts.class)
				.setParameter("expiryDate",
						Date.from(LocalDate.now().plusDays(5).atStartOfDay(defaultZoneId).toInstant()))
				.setParameter("fromDate", Date.from(LocalDate.now().atStartOfDay(defaultZoneId).toInstant()))
				.getResultList();

		Map<Long, List<Accounts>> maps = followups.stream().collect(Collectors.groupingBy(Accounts::getCreatedBy));
		List<MailObj> adminList = new ArrayList<>();
		maps.forEach((uid, lis) -> {
			User user = userDao.getUser(uid);
			List<MailObj> fmoLis = new ArrayList<>();
			lis.forEach(g -> {
				MailObj fmo = new MailObj();
				fmo.setName(user.getName());
				fmo.setType(g.getOrder().getType());

				fmoLis.add(fmo);
				adminList.add(fmo);
			});
			mailService.sendFollowupsMail(user, fmoLis);

		});

		if (adminList.size() > 0) {
			logger.info(" Payment Alerts");
			mailService.sendAdminFollowupsMail(adminList);
		} else {
			logger.info("NO Payment ");
		}
	}

	@Scheduled(cron = "0 0/15 * 1/1 * ?")
	public void lowStockAlert() {

	}
}
