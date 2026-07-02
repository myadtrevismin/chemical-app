package com.core.category.projection;

import java.util.List;

import org.springframework.data.rest.core.config.Projection;

import com.core.category.Category;

@Projection(name = "category_child", types = { Category.class })
public interface CategoryChildProjection {
	Long getId();

	String getName();

	List<CategoryChildProjection> getChildren();
	
	String getLineage();
}
