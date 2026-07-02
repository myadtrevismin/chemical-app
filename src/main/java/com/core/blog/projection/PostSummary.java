package com.core.blog.projection;

import java.util.Date;

import org.springframework.data.rest.core.config.Projection;

import com.core.blog.Blog;

@Projection(name = "post_summary", types = { Blog.class })
public interface PostSummary {
	Long getId();
	
	String getTitle();
	
	String getDescription();

	String getUrl();
	

	
	long getHits();

	Date getCreationDate();
	
	boolean isActive();
	
	boolean isPopular();
}
