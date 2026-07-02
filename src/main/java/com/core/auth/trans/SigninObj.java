package com.core.auth.trans;

public class SigninObj {
	private String userName;
	private String password;
	private String admintoken;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAdmintoken() {
		return admintoken;
	}

	public void setAdmintoken(String admintoken) {
		this.admintoken = admintoken;
	}
}
