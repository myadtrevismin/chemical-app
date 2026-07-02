package com.core.auth.trans;

public class RegistrationObj extends SigninObj {
	private String email;
	private String phone;
	private String code;
	private String name;
	private String lname;
	private String password;
	private boolean vendor;
	private String project;
	private String builder;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.setUserName(email);
		this.email = email;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isVendor() {
		return vendor;
	}

	public void setVendor(boolean vendor) {
		this.vendor = vendor;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getBuilder() {
		return builder;
	}

	public void setBuilder(String builder) {
		this.builder = builder;
	}

}
