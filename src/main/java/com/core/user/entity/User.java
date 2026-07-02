package com.core.user.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import com.core.role.UserBaseRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.org.company.Company;

@Entity
@Table(name = "user")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class User implements Serializable {

	private static final long serialVersionUID = 6635168778513767582L;

	@Id
	@SequenceGenerator(name = "id_sequence", sequenceName = "id_sequence", allocationSize = 5)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_sequence")
	@Column(unique = true, nullable = false)
	private long id;

	@NotNull
	private String name;

	@NotNull
	private String email;

	private String mobile;

	@Column(name = "country_code")
	private String countryCode;

	private String lname;

	private String gender;

	private Date dob;

	private String location;

	@ManyToOne
	@JoinColumn(name = "role")
	private UserBaseRole role;

	@JsonIgnore
	@Column(name = "confirm_token")
	private String confirmToken;

	@JsonIgnore
	@Column(name = "mobile_otp")
	private String mobileOtp;

	@JsonIgnore
	@Column(name = "email_otp")
	private String emailOtp;

	@JsonIgnore
	private boolean enabled;

	@NotNull
	@JsonProperty(access = Access.WRITE_ONLY)
	private String password;

	@JsonIgnore
	private String googleid;

	@Column(name = "referral_code")
	private String referralCode;

	@JsonIgnore
	private String facebookid;

	@JsonProperty(access = Access.READ_ONLY)
	@Column(name = "creation_date")
	private Date creationDate;

	private boolean onboarding;

	public User() {
	}

	public User(long id) {
		this.id = id;
	}

	public User(long id, String name, String email) {
		this.id = id;
		this.name = name;
		this.email = email;
	}

	public User(String name, String email, String mobile) {
		this.name = name;
		this.email = email;
		this.mobile = mobile;
	}

	public User(Company company) {
		this.name = company.getName();
		this.email = company.getEmail();
		this.mobile = company.getPhone();
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
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

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGoogleid() {
		return googleid;
	}

	public void setGoogleid(String googleid) {
		this.googleid = googleid;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date createddate) {
		this.creationDate = createddate;
	}

	public String getFacebookid() {
		return facebookid;
	}

	public void setFacebookid(String facebookid) {
		this.facebookid = facebookid;
	}

	public UserBaseRole getRole() {
		return role;
	}

	public void setRole(UserBaseRole role) {
		this.role = role;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getReferralCode() {
		return referralCode;
	}

	public void setReferralCode(String referralCode) {
		this.referralCode = referralCode;
	}

	public String getConfirmToken() {
		return confirmToken;
	}

	public void setConfirmToken(String confirmToken) {
		this.confirmToken = confirmToken;
	}

	public String getMobileOtp() {
		return mobileOtp;
	}

	public void setMobileOtp(String mobileOtp) {
		this.mobileOtp = mobileOtp;
	}

	public String getEmailOtp() {
		return emailOtp;
	}

	public void setEmailOtp(String emailOtp) {
		this.emailOtp = emailOtp;
	}

	public boolean isOnboarding() {
		return onboarding;
	}

	public void setOnboarding(boolean onboarding) {
		this.onboarding = onboarding;
	}
}
