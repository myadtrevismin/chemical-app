package com.org.sales.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import com.core.common.CommonData;
import com.core.common.CommonUtils;
import com.core.mail.MailService;
import com.core.mail.SalesMailObj;
import com.core.notification.Notification;
import com.core.notification.NotificationService;
import com.org.sales.SalesUser;

@Component
@RepositoryEventHandler(SalesUser.class)
public class SalesUserListener {
	@Autowired
	MailService mailService;
	
	@Autowired
	NotificationService notificationService;

	@HandleAfterCreate
	public void validateAfterSave(SalesUser post) {
		SalesMailObj obj = new SalesMailObj();
		obj.setCode(post.getReference().getCode());
		obj.setType("sales");
		obj.setUrl(CommonData.AdminUrl + "sales/" + post.getReference().getId());
		mailService.sendSalesPurchaseEnquiryAssignedMail(obj, post.getUser());
		
		
		Notification notification = new Notification();
		notification.setUid(post.getUser().getId());
		notification.setType("SALES_ASSIGNED");
		notification.setDescription("Sale Order "+post.getReference().getCode()+" has been assigned by "+CommonUtils.getUserFromSession().getName());
		notification.setUrl("/sales/"+post.getReference().getId());
		notificationService.saveNotification(notification);
		
	}

}
