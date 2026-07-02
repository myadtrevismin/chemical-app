package com.org.purchase.projection;

import org.springframework.data.rest.core.config.Projection;

import com.org.product.projection.ProductAutoSuggestProjection;
import com.org.purchase.PurchaseProduct;

@Projection(name = "purchases-product", types = { PurchaseProduct.class })
public interface PurchaseProductProjection {
	long getId();

	int getSlno();

	int getQuantity();

	double getAmount();
	String getUom();
	
	ProductAutoSuggestProjection getProduct();
}
