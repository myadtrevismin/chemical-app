package com.core.contact.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import com.core.contact.Contact;
import com.core.contact.mailchimp.MailChimpMember;
import com.core.contact.mailchimp.MailChimpService;
import com.core.contact.mailchimp.MergeFields;

@Component
@RepositoryEventHandler(Contact.class)
public class ContactListener {

	@Autowired
	MailChimpService mailChimpService;

	@HandleBeforeCreate
	public void neforePersist(Contact contact) {
		updateMailChimp(contact);
	}

	private void updateMailChimp(Contact contact) {

		if (contact.getEmail() != null) {
			MailChimpMember mcm = new MailChimpMember();
			mcm.setEmailAddress(contact.getEmail());

			mcm.setStatus("subscribed");
			MergeFields mergeFields = new MergeFields();
			mergeFields.setFirstName(contact.getFirstName());
			mergeFields.setLastName(contact.getLastName());
			mergeFields.setPhone(contact.getMobile());
			mcm.setMergeFields(mergeFields);

			if (contact.getMailChimpMember() != null) {
				String id = mailChimpService.updateContact(mcm, contact.getMailChimpMember());
				contact.setMailChimpMember(id);
			} else {
				String id = mailChimpService.createContact(mcm);
				contact.setMailChimpMember(id);
			}

		}
	}

	@HandleBeforeSave
	public void afterSave(Contact contact) {
		updateMailChimp(contact);
	}
}
