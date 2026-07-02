package com.core.blog.projection;

import java.util.Date;

import org.springframework.data.rest.core.config.Projection;

import com.core.blog.Blog;

@Projection(name = "post_web", types = { Blog.class })
public interface PostWeb {
	Long getId();

	String getTitle();

	String getDescription();
	
	String getUrl();
	

	
	boolean isPopular();

	Date getCreationDate();
}
