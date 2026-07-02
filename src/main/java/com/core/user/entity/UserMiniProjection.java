package com.core.user.entity;

import org.springframework.data.rest.core.config.Projection;

@Projection(name = "user_details_mini", types = { User.class })
public interface UserMiniProjection {
	long getId();

	String getName();
}
