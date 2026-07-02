package com.core.role.projection;

import org.springframework.data.rest.core.config.Projection;

import com.core.role.UserBaseRolePermission;

@Projection(name = "user_role_permission", types = { UserBaseRolePermission.class })
public interface UserBaseRolePermissionProjection {
	Long getId();
	boolean isSelected();
	UserBasePermissionProjection getPermission();
}
