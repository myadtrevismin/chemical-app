package com.core.common;

import java.util.List;

import com.core.auth.trans.UserSessionStore;
import com.core.sms.SMSRequest;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Ack {

	private String entityId;

	private Long subEntityId;

	private String name;

	private String message;

	private int status;

	private String url;

	private String token;

	private List<String> errorList;

	List<SMSRequest> lis;

	private long credits;

	private UserSessionStore user;

	private Object object;

	public Ack() {
		super();
	}

	public Ack(String entityId) {
		super();
		this.status = 200;
		this.entityId = entityId;
	}

	public Ack(String message, int status) {
		super();
		this.message = message;
		setStatus(status);
	}

	public Ack(String name, String message, int status) {
		super();
		this.name = name;
		this.message = message;
		setStatus(status);
	}

	public Ack(long entityId, String name, String message, int status) {
		super();
		this.entityId = String.valueOf(entityId);
		this.name = name;
		this.message = message;
		setStatus(status);
	}

	public Ack(String entityId, String name, String message, int status) {
		super();
		this.entityId = entityId;
		this.name = name;
		this.message = message;
		setStatus(status);
	}

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public void setEntityId(long entityId) {
		this.entityId = entityId + "";
	}

	public Long getSubEntityId() {
		return subEntityId;
	}

	public void setSubEntityId(Long subEntityId) {
		this.subEntityId = subEntityId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
		CommonUtils.setResponseStatus(status);
	}

	public List<String> getErrorList() {
		return errorList;
	}

	public void setErrorList(List<String> errorList) {
		this.errorList = errorList;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public UserSessionStore getUser() {
		return user;
	}

	public void setUser(UserSessionStore user) {
		this.user = user;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public long getCredits() {
		return credits;
	}

	public void setCredits(long credits) {
		this.credits = credits;
	}

	public List<SMSRequest> getLis() {
		return lis;
	}

	public void setList(List<SMSRequest> lis) {
		this.lis = lis;
	}

}
