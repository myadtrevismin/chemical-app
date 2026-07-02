package com.core.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.core.common.Ack;
import com.core.common.CommonDao;
import com.core.common.CommonUtils;
import com.core.forms.entity.Feedback;

@RestController
public class PublicController {
	@Autowired
	CommonDao commonDao;
	
	@Value("${app.company.images}")
	String imagePath;
	
	@Value("${server.servlet.context-path}")
	String contextPath;
	
	@PreAuthorize("isFullyAuthenticated()")
	@RequestMapping(value = "{type}-images/{imageId}", method = RequestMethod.POST)
	public void getSaveUserImage(@PathVariable String type, @PathVariable("imageId") String imageId,
			@RequestParam("file") MultipartFile imgfile) throws Exception {
		IOUtils.copy(imgfile.getInputStream(), new FileOutputStream(imagePath + "/images/" + type + "/" + imageId));
	}
	
	@ResponseBody
	@GetMapping("{type}-images/{imageId}")
	public void getPostImage(HttpServletResponse response, @PathVariable String type, @PathVariable("imageId") String imageId) {
		try {
			InputStream is = new FileInputStream(imagePath + "images/" + type + "/" + imageId);
			CommonUtils.sendImageResponse(response, is);
		} catch (FileNotFoundException e) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}
	
	@RequestMapping(value = "feedbacks", method = RequestMethod.POST)
	public Ack saveFeedBack(@RequestBody Feedback fb) {
		return commonDao.createFeedBack(fb);
	}
	
	
	@RequestMapping(value = "subscribe", method = RequestMethod.POST)
	public Ack subscribe(HttpServletRequest request) {
		Ack ack = null;
		
		String email = request.getParameter("email");
		if (CommonUtils.isNotNullOrEmpty(email) && CommonUtils.validateEmail(email)) {			
			commonDao.subscribe(email);
			
			ack = new Ack("Subscribed", "Successfully subscribed.", 201);
		} else {
			ack = new Ack("Unable to Subscribe", "Invalid Email Id.", 400);
		}
		
		return ack;
	}

	@RequestMapping(value = "unsubscribe", method = RequestMethod.POST)
	public Ack unsubscribe(@RequestParam String email, @RequestParam String token) {
		Ack ack = null;
		String errorMsg = null;
		
		if (CommonUtils.isNotNullOrEmpty(email)) {
			if (!commonDao.unsubscribe(email, token)) {
				errorMsg = "Wrong email / token.";
			}
		} else {
			errorMsg = "Email Id not found.";
		}
		
		if(errorMsg == null) {
			ack = new Ack("Unsubscribed", "Successfully unsubscribed.", 200);
		} else {
			ack = new Ack("Unable to unsubscribe", errorMsg, 400);
		}

		return ack;
	}
	
}
