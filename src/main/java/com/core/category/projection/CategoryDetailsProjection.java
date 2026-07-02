package com.core.category.projection;

import org.springframework.data.rest.core.config.Projection;

import com.core.category.Category;

@Projection(name = "category_details", types = { Category.class })
public interface CategoryDetailsProjection {
	Long getId();

	String getName();

	int getSlno();

	boolean isActive();

	String getLineage();
}
