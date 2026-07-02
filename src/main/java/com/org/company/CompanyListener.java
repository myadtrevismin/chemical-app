package com.org.company;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import com.core.common.CommonData;
import com.core.common.CommonUtils;
import com.core.common.Constants;
import com.core.mail.MailService;
import com.core.notification.Notification;
import com.core.notification.NotificationService;

@Component
@RepositoryEventHandler(Company.class)
public class CompanyListener {
	@Autowired
	MailService mailService;

	@Autowired
	NotificationService notificationService;

	@HandleBeforeCreate
	public void validateBeforeCreate(Company post) {

	}

	@HandleAfterCreate
	public void validateAfterCreate(Company post) {
		mailService.sendNewCompanyMail(post, CommonUtils.getUserFromSession());
		Notification notification = new Notification();
		String type = "B".equals(post.getType()) ? "Buyer" : "Vendor";
		notification.setUid(Constants.adminId);
		notification.setType("NEW_COMPANY");
		notification.setUrl("/companies/" + post.getId());
		notification.setDescription("New " + type + " " + post.getName() + " has been added by "
				+ CommonUtils.getUserFromSession().getName());
		notificationService.saveNotification(notification);
		if (Constants.adminId != CommonUtils.getUserIdFromSession()) {
			notification = new Notification();
			notification.setUid(CommonUtils.getUserIdFromSession());
			notification.setType("NEW_COMPANY");
			notification.setUrl("/companies/" + post.getId());
			notification.setDescription("New " + type + " " + post.getName() + " has been added by "
					+ CommonUtils.getUserFromSession().getName());
			notificationService.saveNotification(notification);
		}
	}

	@HandleAfterSave
	public void validateAfterSave(Company post) {
	}

}
