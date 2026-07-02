package com.org.order.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import com.core.common.CommonUtils;
import com.core.event.service.EventService;
import com.org.order.Order;
import com.org.order.OrderUser;
import com.org.order.repo.OrderUserRepo;

@Component
@RepositoryEventHandler(Order.class)
public class OrderListener {

	@Autowired
	EventService eventService;

	@Autowired
	OrderUserRepo orderUserRepo;

	@HandleBeforeCreate
	public void validateBeforeCreate(Order post) {

	}

	@HandleAfterCreate
	public void validateAfterCreate(Order post) {
		if (!CommonUtils.getUserSessionStore().getPermissions().contains("MG_AC")) {
			OrderUser su = new OrderUser();
			su.setUser(CommonUtils.getUserFromSession());
			su.setReference(post);
			orderUserRepo.save(su);
		}

	}

	@HandleAfterSave
	public void validateAfterSave(Order post) {
		if ("Completed".equals(post.getStatus())) {
			eventService.orderCompleted(post);
		}
	}

}
