package com.core.user.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.core.common.CommonUtils;
import com.core.user.entity.User;

@Component
@RepositoryEventHandler(User.class)
public class UserEventHandler {
	@Autowired
	BCryptPasswordEncoder passwordEncoder;

	@HandleBeforeSave
	public void beforeSave(User user) {
		validate(user);

		if (user.getPassword() != null && user.getPassword().length() <= 20) {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
		}

	}

	@HandleBeforeCreate
	public void beforeCreate(User user) {
		validate(user);

		if (user.getPassword() != null && user.getPassword().length() <= 20) {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
		}
		user.setEnabled(true);
	}

	private void validate(User user) {
		boolean reject = false;

		if ("ROLE_AGENT".equals(user.getRole()) || "ROLE_VENDOR".equals(user.getRole())) {
			if (!"ROLE_ADMIN".equals(CommonUtils.getUserSessionStore().getRole())) {
				reject = true;
			}
		}

		if (user.getId() != 0 && user.getId() == CommonUtils.getUserIdFromSession()) {
			reject = false;
		}

		if (reject) {
			throw new RuntimeException("No Privilates to create this user");
		}
		
		
	}

	@HandleAfterSave
	public void afterSave(User user) {

	}

	@HandleAfterCreate
	public void afterCreate(User user) {

	}

}
