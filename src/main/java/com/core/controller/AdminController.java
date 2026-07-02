package com.core.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.core.common.Ack;
import com.core.service.AdminService;
import com.core.user.entity.User;

@RestController
@RequestMapping("admin")
@Secured({ "ROLE_ADMIN" })
public class AdminController {
	@Autowired
	AdminService adminService;

	@RequestMapping(value = "users", method = RequestMethod.POST)
	public User postUser(@Valid @RequestBody User user) {
		adminService.saveUser(user);

		return user;
	}

	@RequestMapping(value = "users/{id}", method = RequestMethod.PATCH)
	public Ack patchUser(@PathVariable("id") long id) {
		return adminService.patchUser(id);
	}
}
