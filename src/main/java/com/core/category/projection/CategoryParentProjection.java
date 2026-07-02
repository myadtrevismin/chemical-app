package com.core.category.projection;

import org.springframework.data.rest.core.config.Projection;

import com.core.category.Category;

@Projection(name = "category_parent", types = { Category.class })
public interface CategoryParentProjection {
	Long getId();

	String getName();

//	String getLineage();
	
	CategoryParentProjection getParent();
}
