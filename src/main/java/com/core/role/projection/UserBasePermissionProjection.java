package com.core.role.projection;

import org.springframework.data.rest.core.config.Projection;

import com.core.role.UserBasePermission;

@Projection(name = "user_permission", types = { UserBasePermission.class })
public interface UserBasePermissionProjection {
	Long getId();
	
	String getDescription();
}
