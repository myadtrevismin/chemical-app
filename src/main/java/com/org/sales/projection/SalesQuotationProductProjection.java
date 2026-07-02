package com.org.sales.projection;

import org.springframework.data.rest.core.config.Projection;

import com.org.product.projection.ProductAutoSuggestProjection;
import com.org.sales.SalesProduct;

@Projection(name = "sales-product", types = { SalesProduct.class })
public interface SalesQuotationProductProjection {
	long getId();
	
	int getSlno();
	
	int getQuantity();
	
	double getAmount();

	String getStatus();
	
	ProductAutoSuggestProjection getProduct();
}
