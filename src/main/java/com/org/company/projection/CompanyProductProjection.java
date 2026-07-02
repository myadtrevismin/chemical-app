package com.org.company.projection;

import org.springframework.data.rest.core.config.Projection;

import com.org.company.CompanyProduct;
import com.org.product.projection.ProductAutoSuggestProjection;

@Projection(name = "company_product", types = { CompanyProduct.class })
public interface CompanyProductProjection {
	long getId();

	ProductAutoSuggestProjection getProduct();
}
