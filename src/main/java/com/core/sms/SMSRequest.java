package com.core.sms;

import java.util.List;

public class SMSRequest {
	private List<String> numbers;
	private String message;
	private String type;
	private String sender;
	private String reference;
	private boolean unicode;
	private String scheduledDate;
	private boolean falsh;

	private int credits;
	
	public List<String> getNumbers() {
		return numbers;
	}

	public void setNumbers(List<String> numbers) {
		this.numbers = numbers;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public boolean isUnicode() {
		return unicode;
	}

	public void setUnicode(boolean unicode) {
		this.unicode = unicode;
	}

	public String getScheduledDate() {
		return scheduledDate;
	}

	public void setScheduledDate(String scheduledDate) {
		this.scheduledDate = scheduledDate;
	}

	public boolean isFalsh() {
		return falsh;
	}

	public void setFalsh(boolean falsh) {
		this.falsh = falsh;
	}

	public int getCredits() {
		return credits;
	}

	public void setCredits(int credits) {
		this.credits = credits;
	}

}
