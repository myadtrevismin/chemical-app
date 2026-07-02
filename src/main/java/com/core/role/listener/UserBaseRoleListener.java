package com.core.role.listener;

import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import com.core.role.UserBaseRole;

@Component
@RepositoryEventHandler(UserBaseRole.class)
public class UserBaseRoleListener {
	@HandleBeforeCreate
	public void validateBeforeCreate(UserBaseRole post) {
		post.getPermissions().forEach(g->g.setRole(post));
		System.out.println();
	}
	@HandleBeforeSave
	public void validateBeforeSave(UserBaseRole post) {
		post.getPermissions().forEach(g->g.setRole(post));
		System.out.println();
	}

	@HandleAfterSave
	public void validateAfterSave(UserBaseRole post) {
	}
}
