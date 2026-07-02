package com.org.purchase.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import com.core.common.CommonUtils;
import com.core.mail.MailService;
import com.core.notification.NotificationService;
import com.org.sales.Sales;
import com.org.sales.SalesUser;
import com.org.sales.repo.SalesUserRepo;

@Component
@RepositoryEventHandler(Sales.class)
public class PurchaseListener {
	@Autowired
	MailService mailService;
	
	@Autowired
	NotificationService notificationService;
	
	@Autowired
	SalesUserRepo salesUserRepo;

	@HandleAfterCreate
	public void validateAfterCreate(Sales post) {
		if(!CommonUtils.getUserSessionStore().getPermissions().contains("MG_AC")) {
			SalesUser su=new SalesUser();
			su.setUser(CommonUtils.getUserFromSession());
			su.setReference(post);
			salesUserRepo.save(su);
		}
		
		 
		
	}

}
