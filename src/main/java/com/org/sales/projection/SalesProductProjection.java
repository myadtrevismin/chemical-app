package com.org.sales.projection;

import org.springframework.data.rest.core.config.Projection;

import com.org.product.projection.ProductAutoSuggestProjection;
import com.org.sales.SalesQuotationProduct;

@Projection(name = "sales-quotation-product", types = { SalesQuotationProduct.class })
public interface SalesProductProjection {
	long getId();

	int getSlno();

	int getQuantity();

	double getAmount();

	String getUom();
	
	ProductAutoSuggestProjection getProduct();
}
