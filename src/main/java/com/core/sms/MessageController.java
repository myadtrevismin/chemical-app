package com.core.sms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.core.common.Ack;

@RestController
@RequestMapping("sms")
@PreAuthorize("isFullyAuthenticated()")
public class MessageController {
	@Autowired
	MessageService messageService;

	@PostMapping
	public Ack sendSms(@RequestBody Message smsObj) {
		Ack ack = null;
		try {
			MessageStatus ms = messageService.submitRequest(smsObj);
			if (ms.isStatus()) {
				ack = new Ack("Request Submitted", 200);
			} else {
				ack = new Ack("Error while sending sms, check for credits and  sender id", 400);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ack = new Ack("Error while processing your request " + e.getMessage(), 500);

		}
		return ack;
	}

	@PostMapping(path = "preview")
	public Ack previewSms(@RequestBody Message smsObj) {
		Ack ack = null;
		try {
			ack = messageService.previewRequest(smsObj);
		} catch (Exception e) {
			e.printStackTrace();
			ack = new Ack("Error while processing your request " + e.getMessage(), 500);

		}
		return ack;
	}

}
