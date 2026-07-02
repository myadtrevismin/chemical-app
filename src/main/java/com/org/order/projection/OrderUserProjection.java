package com.org.order.projection;

import org.springframework.data.rest.core.config.Projection;

import com.core.user.entity.UserMiniProjection;
import com.org.order.OrderUser;

@Projection(name = "orders-user", types = { OrderUser.class })
public interface OrderUserProjection {
	Long getId();

	UserMiniProjection getUser();
}
