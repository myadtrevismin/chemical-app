package com.org.sales.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import com.core.common.CommonUtils;
import com.core.mail.MailService;
import com.core.notification.NotificationService;
import com.org.purchase.Purchase;
import com.org.purchase.PurchaseUser;
import com.org.purchase.repo.PurchaseUserRepo;

@Component
@RepositoryEventHandler(Purchase.class)
public class SalesListener {
	@Autowired
	MailService mailService;
	
	@Autowired
	NotificationService notificationService;
	
	@Autowired
	PurchaseUserRepo salesUserRepo;

	@HandleAfterCreate
	public void validateAfterCreate(Purchase post) {
		if(!CommonUtils.getUserSessionStore().getPermissions().contains("MG_AC")) {
			PurchaseUser su=new PurchaseUser();
			su.setUser(CommonUtils.getUserFromSession());
			su.setReference(post);
			salesUserRepo.save(su);
		}
		
		 
		
	}

}
