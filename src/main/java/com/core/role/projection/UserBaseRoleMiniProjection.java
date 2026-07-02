package com.core.role.projection;

import org.springframework.data.rest.core.config.Projection;

import com.core.role.UserBaseRole;

@Projection(name = "role_auto_suggest", types = { UserBaseRole.class })
public interface UserBaseRoleMiniProjection {
	Long getId();

	String getName();
	
	String getCode();
}
