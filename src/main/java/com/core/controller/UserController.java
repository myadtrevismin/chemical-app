package com.core.controller;

import java.io.IOException;
import java.text.ParseException;

import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.core.auth.dao.UserDao;
import com.core.auth.trans.UserSessionStore;
import com.core.common.Ack;
import com.core.common.CommonUtils;
import com.core.trans.Dashboard;
import com.core.trans.NewOperations;
import com.core.user.entity.User;

@RestController
@RequestMapping("user")
@PreAuthorize("isFullyAuthenticated()")
public class UserController {
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	UserDao userDao;
	
	@RequestMapping(method = RequestMethod.GET)
	public UserSessionStore getUserSessionStore() {
		return CommonUtils.getUserSessionStore(); 
	}
	
	@RequestMapping(value = "dashboard", method = RequestMethod.GET)
	public Dashboard getUserDashboard() {
		return userDao.getUserDashboard();
	}
	
	@RequestMapping(value = "new-operations", method = RequestMethod.GET)
	public NewOperations getNewOperations() {
		return userDao.getNewOperations();
	}
	
	@RequestMapping(value = "profile", method = RequestMethod.GET)
	public User getUserDetails() {
		return userDao.getUserDetails(CommonUtils.getUserIdFromSession());
	}
	
	@RequestMapping(value = "profile", method = RequestMethod.POST)
	public Ack updateUserDetails(@RequestBody User user) throws ParseException {
		Ack ack = new Ack();
		
		userDao.saveUserWithProfile(user);

		ack.setStatus(HttpStatus.OK.value());
		ack.setMessage("User Saved");

		return ack;
	}
	
	@RequestMapping(value = "email", method = RequestMethod.POST)
	public Ack saveChangedpassword(@RequestParam String mailId) {
		Ack ack = null;
		if (CommonUtils.isNotNullOrEmpty(mailId) && CommonUtils.validateEmail(mailId)) {
			UserSessionStore uss = CommonUtils.getUserSessionStore();
			
			ack = userDao.changeEmailId(uss, mailId);
		} else {
			ack = new Ack();
			ack.setMessage("Invalid email provided");
			ack.setStatus(400);
			return ack;
		}
		
		return ack;
	}

	@RequestMapping(value = "password", method = RequestMethod.POST)
	public Ack saveChangedpassword(@RequestParam String oldPassword, @RequestParam String newPassword) {
		if (newPassword != null) {
			return userDao.changePassword(oldPassword, newPassword);
		} else {
			Ack ack = new Ack();
			ack.setMessage("No New password provided");
			return ack;
		}
	}

	@RequestMapping(value = "image", method = RequestMethod.POST)
	public Ack getSaveUserImage(@RequestParam("image") MultipartFile imgfile) {
		Ack ack = new Ack();
		try {
			ack.setUrl(userDao.updateUserImage(imgfile.getBytes()));
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			ack.setStatus(400);
		}

		return ack;
	}
	
	
	@RequestMapping(value = "mobile-image", method = RequestMethod.POST)
	public Ack getSaveUserImage(@RequestParam("image") String base64Image) {
		Ack ack = new Ack();
		
		ack.setUrl(userDao.updateUserImage(DatatypeConverter.parseBase64Binary(base64Image)));

		return ack;
	}
	 
}
