package com.core.role.projection;

import java.util.List;

import org.springframework.data.rest.core.config.Projection;

import com.core.role.UserBaseRole;

@Projection(name = "user_role_detail", types = { UserBaseRole.class })
public interface UserBaseRoleDetailProjection {
	Long getId();

	String getName();

	String getCode();

	String getDescription();

	boolean isDefaultRole();

	List<UserBaseRolePermissionProjection> getPermissions();
}
