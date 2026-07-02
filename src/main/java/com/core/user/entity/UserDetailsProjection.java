package com.core.user.entity;

import java.util.Date;

import org.springframework.data.rest.core.config.Projection;

import com.core.role.projection.UserBaseRoleMiniProjection;

@Projection(name = "user_details", types = { User.class })
public interface UserDetailsProjection {
	long getId();

	String getName();

	String getEmail();

	String getMobile();

	UserBaseRoleMiniProjection getRole();

	boolean getEnabled();

	Date getCreationDate();
}
