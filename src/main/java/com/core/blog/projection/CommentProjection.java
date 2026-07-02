package com.core.blog.projection;

import org.springframework.data.rest.core.config.Projection;

import com.core.blog.BlogComment;

@Projection(name = "comment_all", types = { BlogComment.class })
public interface CommentProjection {
	Long getId();

	String getComment();

	String getName();

	String getCreationDate();

	boolean isActive();
}
