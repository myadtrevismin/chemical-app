package com.core.auth.trans;

public class RefreshToken {
	private String token;
	
	private long expiry;

	public String getToken() {
		return token;
	}

	public void setToken(String refreshToken) {
		this.token = refreshToken;
	}

	public long getExpiry() {
		return expiry;
	}

	public void setExpiry(long expiresAt) {
		this.expiry = expiresAt;
	}
}