package com.core.event.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import jakarta.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.core.common.CommonData;
import com.core.common.CommonUtils;
import com.core.event.Event;
import com.core.event.repo.EventRepo;
import com.core.mail.MailService;
import com.org.common.MailObj;
import com.org.order.Order;
import com.org.order.OrderProduct;
import com.org.product.Product;
import com.org.product.ProductFlow;
import com.org.product.repo.ProductFlowRepo;

@Service
public class EventService {
	@Autowired
	EventRepo eventRepo;

	@Autowired
	ProductFlowRepo flowRepo;

	@Autowired
	MailService mailService;

	@Autowired
	EntityManager em;

	@Transactional
	public Event saveEvent(Event event) {

		return eventRepo.save(event);
	}

	@Transactional
	public void updateEvent(Event event, Long eventId) {

		Optional<Event> eventOpt = eventRepo.findById(eventId);
		if (eventOpt.isPresent()) {
			Event saved = eventOpt.get();
			saved.setTitle(event.getTitle());
			saved.setDescription(event.getDescription());
			saved.setFromTime(event.getFromTime());
			saved.setToTime(event.getToTime());
			eventRepo.save(event);
		}

	}

	@Transactional
	public void updateEvent(Event event) {

		Optional<Event> eventOpt = eventRepo.findByTypeAndParent(event.getType(), event.getParent());
		if (eventOpt.isPresent()) {
			Event saved = eventOpt.get();
			saved.setTitle(event.getTitle());
			saved.setDescription(event.getDescription());
			saved.setFromTime(event.getFromTime());
			saved.setToTime(event.getToTime());
			eventRepo.save(event);
		}
	}

	@Transactional
	public void orderCompleted(Order post) {
		Set<OrderProduct> lis = post.getProducts();
		List<ProductFlow> pfList = flowRepo.findByReferenceAndRefType(post.getId(), "ORDER");
		if (pfList.size() == 0) {
			/*
			 * for (OrderProduct op : lis) { Product product = op.getProduct(); ProductFlow
			 * pf = new ProductFlow(); pf.setQuantity(op.getQuantity());
			 * pf.setProduct(product); pf.setType("Sales".equals(post.getType()) ?
			 * "Outgoing" : "Incoming"); pf.setDate(new Date()); pf.setRefType("ORDER");
			 * pf.setReference(post.getId()); flowRepo.save(pf);
			 * 
			 * updateProductQty(pf.getType(), (long) pf.getQuantity(), product.getId()); }
			 */
			MailObj mailObj = new MailObj();
			mailObj.setUrl(CommonData.AdminUrl + "orders/" + post.getId());
			mailService.sendOrderCompleted(mailObj, CommonUtils.getUserFromSession());
		}

	}

	@Transactional
	public void updateProductQty(String type, Long qty, Long prodId) {
		String query = "update Product set quantity=quantity-:amount where id=:prodId";

		if ("InComing".equals(type)) {
			query = "update Product set quantity=quantity+:amount where id=:prodId";
		}
		
		em.createQuery(query).setParameter("amount", qty).setParameter("prodId", prodId).executeUpdate();

	}
}
