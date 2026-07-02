package com.core.category.projection;

import java.util.List;

import org.springframework.data.rest.core.config.Projection;

import com.core.category.Category;

@Projection(name = "category_all", types = { Category.class })
public interface CategoryAllProjection {
	Long getId();

	String getName();

	int getSlno();

	boolean isActive();
	
	List<CategoryAllProjection> getChildren();
	
	String getLineage();
}
