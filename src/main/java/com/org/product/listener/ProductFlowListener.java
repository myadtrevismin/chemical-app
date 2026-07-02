package com.org.product.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import com.core.common.CommonData;
import com.core.common.CommonUtils;
import com.core.common.Constants;
import com.core.event.service.EventService;
import com.core.mail.MailService;
import com.core.notification.Notification;
import com.core.notification.NotificationService;
import com.org.common.MailObj;
import com.org.product.ProductFlow;

@Component
@RepositoryEventHandler(ProductFlow.class)
public class ProductFlowListener {
	@Autowired
	MailService mailService;

	@Autowired
	EventService eventService;
	
	@Autowired
	NotificationService notificationService;

	@HandleBeforeCreate
	public void validateBeforeCreate(ProductFlow post) {

	}

	@HandleAfterCreate
	public void validateAfterCreate(ProductFlow post) {
		MailObj obj = new MailObj();
		obj.setType("InComing".equals(post.getType()) ? " increased " : " decreased ");
		obj.setQty(post.getQuantity());
		obj.setName(post.getProduct().getName());
		obj.setUrl(CommonData.AdminUrl + "products/" + post.getProduct().getId());
		mailService.sendStockChangeAlert(obj, CommonUtils.getUserFromSession());
		eventService.updateProductQty(post.getType(), (long) post.getQuantity(), post.getProduct().getId());
		
		String desc="New stock "+("InComing".equals(post.getType()) ? " added " : " deleted ")+" to Product "+post.getProduct().getName()+" by "+CommonUtils.getUserFromSession().getName();
		Notification notification = new Notification();
		notification.setUid(Constants.adminId);
		notification.setType("FLOW_UPDATED");
		notification.setDescription(desc);
		notification.setUrl("/products/"+post.getProduct().getId());
		notificationService.saveNotification(notification);
		if (Constants.adminId != CommonUtils.getUserIdFromSession()) {
			notification = new Notification();
			notification.setUid(CommonUtils.getUserIdFromSession());
			notification.setType("FLOW_UPDATED");
			notification.setDescription(desc);
			notification.setUrl("/products/"+post.getProduct().getId());

			notificationService.saveNotification(notification);
		}
		
	}

	@HandleAfterSave
	public void validateAfterSave(ProductFlow post) {
	}

}
