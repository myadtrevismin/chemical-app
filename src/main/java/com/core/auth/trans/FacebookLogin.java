package com.core.auth.trans;


public class FacebookLogin   {
	private String email;
	private String singedRequest;
	private String accessToken;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSingedRequest() {
		return singedRequest;
	}

	public void setSingedRequest(String singedRequest) {
		this.singedRequest = singedRequest;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
}
