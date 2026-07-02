package com.org.followup;

import java.util.Optional;

import jakarta.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import com.core.common.CommonData.TypeRepoEnum;
import com.core.common.CommonUtils;
import com.core.common.Constants;
import com.core.event.Event;
import com.core.event.service.EventService;
import com.core.lead.Lead;
import com.core.lead.repo.LeadRepo;
import com.core.notification.Notification;
import com.core.notification.NotificationService;
import com.org.order.Order;
import com.org.order.repo.OrderRepo;
import com.org.purchase.Purchase;
import com.org.purchase.repo.PurchaseRepo;
import com.org.sales.Sales;
import com.org.sales.repo.SalesRepo;

@Component
@RepositoryEventHandler(Followup.class)
public class FollowupListener {

	@Autowired
	EventService eventService;

	@Autowired
	NotificationService notificationService;

	@Autowired
	SalesRepo salesRepo;

	@Autowired
	PurchaseRepo purchaseRepo;

	@Autowired
	OrderRepo orderRepo;

	@Autowired
	LeadRepo leadRepo;
	
	@Autowired
	EntityManager em;
	
	@HandleBeforeSave
	public void validateBeforeSave(Followup post) {
	}

	@HandleBeforeCreate
	public void validateBeforeCreate(Followup post) {

	}

	@HandleAfterSave
	public void validateAfterSave(Followup post) {
		saveEvent(post, false);
	}

	private void saveEvent(Followup post, boolean isNew) {
		Event event = new Event();
		event.setTitle("Followup : ");
		event.setFromTime(post.getNextFollowUpDate());
		event.setToTime(post.getNextFollowUpDate());
		event.setDescription("Type: " + post.getNextFollowupType() + " ,Contact: " + post.getContact());
		event.setType(post.getRepository());
		event.setParent(post.getId());
		event.setAllDay(true);
		event.setUid(CommonUtils.getUserIdFromSession());
		if (isNew) {
			eventService.saveEvent(event);
		} else {
			eventService.updateEvent(event);
		}
		if (Constants.adminId != CommonUtils.getUserIdFromSession()) {
			em.detach(event);
			event.setId(null);
			event.setUid(Constants.adminId);
			if (isNew) {
				eventService.saveEvent(event);
			} else {
				eventService.updateEvent(event);
			}
		}
	}

	@HandleAfterCreate
	public void validateAfterCreate(Followup post) {
		saveEvent(post, true);
		TypeRepoEnum repo = TypeRepoEnum.valueOf(post.getRepository());
		String desc = null;
		String url=null;
		if (TypeRepoEnum.sales == repo) {
			Optional<Sales> sopt = salesRepo.findById(post.getReference());
			if (sopt.isPresent()) {
				desc = "Follow up added to " + post.getRepository() + " order " + sopt.get().getCode() + " by "
						+ CommonUtils.getUserFromSession().getName();
				url="/sales/"+post.getReference();
			}
		} else if (TypeRepoEnum.purchases == repo) {
			Optional<Purchase> sopt = purchaseRepo.findById(post.getReference());
			if (sopt.isPresent()) {
				desc = "Follow up added to " + post.getRepository() + " order " + sopt.get().getCode() + " by "
						+ CommonUtils.getUserFromSession().getName();
				url="/purchases/"+post.getReference();
			}
		} else if (TypeRepoEnum.orders == repo) {
			Optional<Order> sopt = orderRepo.findById(post.getReference());
			if (sopt.isPresent()) {
				desc = "Follow up added to " + post.getRepository() + " order " + sopt.get().getCode() + " by "
						+ CommonUtils.getUserFromSession().getName();
				url="/orders/"+post.getReference();
			}
		} else if (TypeRepoEnum.leads == repo) {
			Optional<Lead> sopt = leadRepo.findById(post.getReference());
			if (sopt.isPresent()) {
				desc = "Follow up added to " + post.getRepository() + " lead " + sopt.get().getName() + " by "
						+ CommonUtils.getUserFromSession().getName();
				url="/leads/"+post.getReference();
			}
		}
		if (desc != null) {
			Notification notification = new Notification();
			notification.setUid(Constants.adminId);
			notification.setType("NEW_FOLLOWUP");
			notification.setDescription(desc);
			notification.setUrl(url);
			notificationService.saveNotification(notification);
			if (Constants.adminId != CommonUtils.getUserIdFromSession()) {
				notification = new Notification();
				notification.setUid(CommonUtils.getUserIdFromSession());
				notification.setType("NEW_FOLLOWUP");
				notification.setDescription(desc);
				notification.setUrl(url);

				notificationService.saveNotification(notification);
			}
		}
	}

}
