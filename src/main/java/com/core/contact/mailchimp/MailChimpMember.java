package com.core.contact.mailchimp;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MailChimpMember {

	@JsonProperty("email_address")
	private String emailAddress;
	@JsonProperty("status")
	private String status;
	@JsonProperty("merge_fields")
	private MergeFields mergeFields;

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public MergeFields getMergeFields() {
		return mergeFields;
	}

	public void setMergeFields(MergeFields mergeFields) {
		this.mergeFields = mergeFields;
	}

}
