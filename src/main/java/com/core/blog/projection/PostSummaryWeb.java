package com.core.blog.projection;

import org.springframework.data.rest.core.config.Projection;

import com.core.blog.Blog;

@Projection(name = "post_summary_web", types = { Blog.class })
public interface PostSummaryWeb {
	Long getId();
	
	String getTitle();

	String getUrl();
	
	boolean isPopular();
}
