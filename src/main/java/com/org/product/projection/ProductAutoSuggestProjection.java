package com.org.product.projection;

import org.springframework.data.rest.core.config.Projection;

import com.org.product.Product;

@Projection(name = "product_auto_suggest", types = { Product.class })
public interface ProductAutoSuggestProjection {
	Long getId();

	String getName();
}
