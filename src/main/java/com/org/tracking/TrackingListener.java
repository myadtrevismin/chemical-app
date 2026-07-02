package com.org.tracking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import com.core.mail.MailService;
import com.core.notification.NotificationService;

@Component
@RepositoryEventHandler(Tracking.class)
public class TrackingListener {
	@Autowired
	MailService mailService;

	@Autowired
	NotificationService notificationService;

	@HandleAfterCreate
	public void validateAfterSave(Tracking post) {
		mailService.sendTrackingMail(post);
	}

}
