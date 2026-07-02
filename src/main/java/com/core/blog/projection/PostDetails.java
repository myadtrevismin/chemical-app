package com.core.blog.projection;

import java.util.Date;

import org.springframework.data.rest.core.config.Projection;

import com.core.blog.Blog;

@Projection(name = "post_details", types = { Blog.class })
public interface PostDetails {
	Long getId();

	String getTitle();
	
	String getDescription();
	
	String getContent();

	String getUrl();
	
	long getHits();
	
	

	Date getCreationDate();

	boolean isActive();
	
	boolean isPopular();
}
