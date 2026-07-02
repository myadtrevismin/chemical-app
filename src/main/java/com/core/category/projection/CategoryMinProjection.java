package com.core.category.projection;

import org.springframework.data.rest.core.config.Projection;

import com.core.category.Category;

@Projection(name = "category_min", types = { Category.class })
public interface CategoryMinProjection {
	Long getId();

	String getName();
}
