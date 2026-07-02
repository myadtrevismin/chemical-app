package com.org.purchase.projection;

import org.springframework.data.rest.core.config.Projection;

import com.core.user.entity.UserMiniProjection;
import com.org.purchase.PurchaseUser;

@Projection(name = "purchases-user", types = { PurchaseUser.class })
public interface PurchaseUserProjection {
	Long getId();

	UserMiniProjection getUser();
}
