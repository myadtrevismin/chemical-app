package com.core.contact;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.core.common.Ack;

@RestController
@RequestMapping(path = "contacts")
@PreAuthorize("isFullyAuthenticated()")
public class ContactsController {

	@Autowired
	ContactService contactService;

	@PostMapping(path = "upload")
	public Ack uploadContacts(@RequestParam("file") MultipartFile file, Long gid) {
		Ack ack = null;
		try {
			contactService.uploadContacts(file, gid);
			ack = new Ack("Success", 200);
		} catch (Exception e) {
			e.printStackTrace();
			ack = new Ack("Errir while uploading contacts", 500);
		}
		return ack;
	}
}
