package com.core.contact.mailchimp;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MergeFields {
	@JsonProperty("FNAME")
	private String firstName;
	@JsonProperty("LNAME")
	private String lastName;
	@JsonProperty("PHONE")
	private String phone;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}
