package com.org.approval;

import java.util.Date;
import java.util.Optional;

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
import com.core.event.service.EventService;
import com.core.notification.Notification;
import com.core.notification.NotificationService;
import com.org.order.Order;
import com.org.order.repo.OrderRepo;
import com.org.purchase.Purchase;
import com.org.purchase.repo.PurchaseRepo;
import com.org.sales.Sales;
import com.org.sales.repo.SalesRepo;

@Component
@RepositoryEventHandler(Approval.class)
public class ApprovalListener {

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

	@HandleBeforeSave
	public void validateBeforeSave(Approval post) {

		if ("A".equals(post.getStatus()) || "R".equals(post.getStatus())) {
			post.setResponseDate(new Date());
		}
	}

	@HandleBeforeCreate
	public void validateBeforeCreate(Approval post) {
		if ("A".equals(post.getStatus()) || "R".equals(post.getStatus())) {
			post.setResponseDate(new Date());
		}
	}

	@HandleAfterSave
	public void validateAfterSave(Approval post) {
	}

	@HandleAfterCreate
	public void validateAfterCreate(Approval post) {
		TypeRepoEnum repo = TypeRepoEnum.valueOf(post.getRepository());
		String desc = null;
		String url = null;
		if (TypeRepoEnum.sales == repo) {
			Optional<Sales> sopt = salesRepo.findById(post.getReference());
			if (sopt.isPresent()) {
				desc = "A " + post.getRepository() + " enqury " + sopt.get().getCode() + " added by "
						+ CommonUtils.getUserFromSession().getName();
				url = "/sales/" + post.getReference();
			}
		} else if (TypeRepoEnum.purchases == repo) {
			Optional<Purchase> sopt = purchaseRepo.findById(post.getReference());
			if (sopt.isPresent()) {
				desc = "Approval requested  purchase " + sopt.get().getCode() + " added by "
						+ CommonUtils.getUserFromSession().getName();
				url = "/purchases/" + post.getReference();
			}
		} else if (TypeRepoEnum.orders == repo) {
			Optional<Order> sopt = orderRepo.findById(post.getReference());
			if (sopt.isPresent()) {
				desc = "Order Approvall request for " + sopt.get().getCode() + " added by "
						+ CommonUtils.getUserFromSession().getName();
				url = "/orders/" + post.getReference();
			}
		}
		if (desc != null) {
			Notification notification = new Notification();
			notification.setUid(Constants.adminId);
			notification.setType("NEW_APPROVAL");
			notification.setDescription(desc);
			notification.setUrl(url);
			notificationService.saveNotification(notification);
			if (Constants.adminId != CommonUtils.getUserIdFromSession()) {
				notification = new Notification();
				notification.setUid(CommonUtils.getUserIdFromSession());
				notification.setType("NEW_APPROVAL");
				notification.setDescription(desc);
				notification.setUrl(url);

				notificationService.saveNotification(notification);
			}
		}
	}

}
