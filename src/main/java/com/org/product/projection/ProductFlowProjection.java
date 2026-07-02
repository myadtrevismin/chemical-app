package com.org.product.projection;

import java.util.Date;

import org.springframework.data.rest.core.config.Projection;

import com.core.user.entity.UserMiniProjection;
import com.org.product.Product;

@Projection(name = "product_flow_list", types = { Product.class })
public interface ProductFlowProjection {
	Long getId();

	String getType();
	
	String getRefType();
	
	Long getReference();
	
	Date getDate();
	
	Date getCreationDate();
	
	
	UserMiniProjection getCreatedBy();
}
