package com.org.order.projection;

import org.springframework.data.rest.core.config.Projection;

import com.org.order.Order;

@Projection(name = "order_auto_suggest", types = { Order.class })
public interface OrderAutoSuggestProjection {
	long getId();

	String getCode();
}
