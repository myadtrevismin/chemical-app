package com.org.product.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import com.core.common.CommonUtils;
import com.core.common.Constants;
import com.core.mail.MailService;
import com.core.notification.Notification;
import com.core.notification.NotificationService;
import com.org.product.Product;

@Component
@RepositoryEventHandler(Product.class)
public class ProductListener {
	@Autowired
	MailService mailService;

	@Autowired
	NotificationService notificationService;

	@HandleBeforeCreate
	public void validateBeforeCreate(Product post) {

	}

	@HandleAfterCreate
	public void validateAfterCreate(Product post) {
		mailService.sendNewProductMail(post, CommonUtils.getUserFromSession());

		Notification notification = new Notification();
		notification.setUid(Constants.adminId);
		notification.setType("NEW_PRODUCT");
		notification.setUrl("/products/" + post.getId());
		notification.setDescription(
				"New product " + post.getName() + " has been added by " + CommonUtils.getUserFromSession().getName());
		notificationService.saveNotification(notification);
		if (Constants.adminId != CommonUtils.getUserIdFromSession()) {
			notification = new Notification();
			notification.setUid(CommonUtils.getUserIdFromSession());
			notification.setType("NEW_PROUCT");
			notification.setUrl("/products/" + post.getId());
			notification.setDescription("New product " + post.getName() + " has been added by "
					+ CommonUtils.getUserFromSession().getName());
			notificationService.saveNotification(notification);
		}
	}

	@HandleAfterSave
	public void validateAfterSave(Product post) {
	}

}
