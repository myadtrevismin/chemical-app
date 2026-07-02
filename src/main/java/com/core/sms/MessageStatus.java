package com.core.sms;

import java.util.ArrayList;
import java.util.List;

public class MessageStatus {
	private boolean status;

	private List<String> messageIds;

	public MessageStatus() {
		messageIds = new ArrayList<String>();
	}
	
	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public List<String> getMessageIds() {
		return messageIds;
	}

	public void setMessageIds(List<String> messageIds) {
		this.messageIds = messageIds;
	}

}
