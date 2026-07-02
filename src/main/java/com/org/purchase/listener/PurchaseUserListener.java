package com.org.purchase.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import com.core.common.CommonData;
import com.core.common.CommonUtils;
import com.core.common.Constants;
import com.core.mail.MailService;
import com.core.mail.SalesMailObj;
import com.core.notification.Notification;
import com.core.notification.NotificationService;
import com.org.purchase.PurchaseUser;

@Component
@RepositoryEventHandler(PurchaseUser.class)
public class PurchaseUserListener {
	@Autowired
	MailService mailService;
	@Autowired
	NotificationService notificationService;

	@HandleAfterCreate
	public void validateAfterSave(PurchaseUser post) {
		SalesMailObj obj = new SalesMailObj();
		obj.setCode(post.getReference().getCode());
		obj.setType("puchase");
		obj.setUrl(CommonData.AdminUrl + "purchases/" + post.getReference().getId());
		mailService.sendSalesPurchaseEnquiryAssignedMail(obj, post.getUser());
		
		
		
		Notification notification = new Notification();
		notification.setUid(post.getUser().getId());
		notification.setType("Purchase_ASSIGNED");
		notification.setDescription("Purchase Order "+post.getReference().getCode()+" has been assigned by "+CommonUtils.getUserFromSession().getName());
		notification.setUrl("/purchases/"+post.getReference().getId());
		notificationService.saveNotification(notification);
	}

}
