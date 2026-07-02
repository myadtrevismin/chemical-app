package com.core.sms;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Message {
	@Id
	private String id;
	
	private String message;
	
	private String sender;
	
	private String type;
	
	private Long group;
	
	private Long template;
	
	private Long custom;
	
	private boolean flash;
	
	private boolean duplicatedAllowed;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	private String number;
	
	private List<String> to;
	
	private boolean unicode;
	
	private boolean scheduled;
	
	private Date scheduledTime;
	
	private long uid;
	
	private int credits;
	
	private Date creationDate;
	
	private boolean fromApi;
	
	 
	
	@Transient
	@JsonIgnore
	private List<String> messages;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getGroup() {
		return group;
	}

	public void setGroup(Long group) {
		this.group = group;
	}

	public Long getCustom() {
		return custom;
	}

	public void setCustom(Long custom) {
		this.custom = custom;
	}

	public Long getTemplate() {
		return template;
	}

	public void setTemplate(Long template) {
		this.template = template;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public List<String> getTo() {
		return to;
	}

	public void setTo(List<String> to) {
		this.to = to;
	}

	public boolean isUnicode() {
		return unicode;
	}

	public void setUnicode(boolean unicode) {
		this.unicode = unicode;
	}

	public boolean isScheduled() {
		return scheduled;
	}

	public void setScheduled(boolean scheduled) {
		this.scheduled = scheduled;
	}

	public Date getScheduledTime() {
		return scheduledTime;
	}

	public void setScheduledTime(Date scheduledTime) {
		this.scheduledTime = scheduledTime;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	 

	public List<String> getMessages() {
		return messages;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}

	public int getCredits() {
		return credits;
	}

	public void setCredits(int credits) {
		this.credits = credits;
	}

	public boolean isFromApi() {
		return fromApi;
	}

	public void setFromApi(boolean fromApi) {
		this.fromApi = fromApi;
	}

	public boolean isFlash() {
		return flash;
	}

	public void setFlash(boolean flash) {
		this.flash = flash;
	}

	public boolean isDuplicatedAllowed() {
		return duplicatedAllowed;
	}

	public void setDuplicatedAllowed(boolean duplicatedAllowed) {
		this.duplicatedAllowed = duplicatedAllowed;
	}

}
