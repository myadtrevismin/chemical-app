package com.core.auth.trans;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({ "password", "authorities", "accountNonExpired", "accountNonLocked", "credentialsNonExpired",
		"enabled" })
public class UserSessionStore extends User {
	private static final long serialVersionUID = 1L;

	private Long id;

	private boolean toggle;

	private String name;

	private boolean emailPending;

	private String imageURL;

	private String timeZone;
	
	private String email;

	private String phone;

	private Date dob;
	
	private String role;
	
	private String token;
	
	private long tokenExpiry;
	
	private String referralCode;
	
	private boolean onboarding;

	@JsonIgnore
	private String googleid;

	@JsonIgnore
	private String facebookid;
	
	@JsonIgnore
	private Date enrolledDate;
	
	
	private boolean kyc;
	
	private boolean riskProfile;
	
	private boolean questionnaire;
	
	private boolean networth;
	
	
	private String profile;
	
	private List<String> permissions;

	public UserSessionStore(String username, String password, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities) {
		// not using enabled - for user login before confirming email
		super(username, password, true, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isToggle() {
		return toggle;
	}

	public void setToggle(boolean toggle) {
		this.toggle = toggle;
	}

	public String getName() {
		return name;
	}

	public void setName(String fname) {
		this.name = fname;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public long getTokenExpiry() {
		return tokenExpiry;
	}

	public void setTokenExpiry(long tokenExpiry) {
		this.tokenExpiry = tokenExpiry;
	}

	public String getGoogleid() {
		return googleid;
	}

	public void setGoogleid(String googleid) {
		this.googleid = googleid;
	}

	public String getFacebookid() {
		return facebookid;
	}

	public void setFacebookid(String facebookid) {
		this.facebookid = facebookid;
	}

	public boolean isEmailPending() {
		return emailPending;
	}

	public void setEmailPending(boolean emailPending) {
		this.emailPending = emailPending;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Date getEnrolledDate() {
		return enrolledDate;
	}

	public void setEnrolledDate(Date enrolledDate) {
		this.enrolledDate = enrolledDate;
	}

	public String getReferralCode() {
		return referralCode;
	}

	public void setReferralCode(String referralCode) {
		this.referralCode = referralCode;
	}

	public boolean isOnboarding() {
		return onboarding;
	}

	public void setOnboarding(boolean onboarding) {
		this.onboarding = onboarding;
	}

	public boolean isKyc() {
		return kyc;
	}

	public void setKyc(boolean kyc) {
		this.kyc = kyc;
	}

	public boolean isRiskProfile() {
		return riskProfile;
	}

	public void setRiskProfile(boolean riskProfile) {
		this.riskProfile = riskProfile;
	}

	public boolean isQuestionnaire() {
		return questionnaire;
	}

	public void setQuestionnaire(boolean questionnaire) {
		this.questionnaire = questionnaire;
	}

	public boolean isNetworth() {
		return networth;
	}

	public void setNetworth(boolean networth) {
		this.networth = networth;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public List<String> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<String> permissions) {
		this.permissions = permissions;
	}

}
