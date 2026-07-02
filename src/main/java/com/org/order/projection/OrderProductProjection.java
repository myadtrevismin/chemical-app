package com.org.order.projection;

import org.springframework.data.rest.core.config.Projection;

import com.org.order.OrderProduct;
import com.org.product.projection.ProductAutoSuggestProjection;

@Projection(name = "orders-product", types = { OrderProduct.class })
public interface OrderProductProjection {
	long getId();

	int getSlno();

	int getQuantity();

	double getAmount();

	ProductAutoSuggestProjection getProduct();
}
