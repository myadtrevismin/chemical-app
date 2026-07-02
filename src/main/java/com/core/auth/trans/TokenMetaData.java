package com.core.auth.trans;

import java.util.Collection;
import java.util.Date;

import org.springframework.security.core.GrantedAuthority;

public class TokenMetaData {
	private String token;

	private String domain;

	private Date validTill;

	private Date lastAccessTime;

	private long studentSessionLogId;

	private Collection<GrantedAuthority> authorities = null;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getLastAccessTime() {
		return lastAccessTime;
	}

	public void setLastAccessTime(Date lastAccessTime) {
		this.lastAccessTime = lastAccessTime;
	}

	public Date getValidTill() {
		return validTill;
	}

	public void setValidTill(Date validTill) {
		this.validTill = validTill;
	}

	@Override
	public int hashCode() {
		return this.token.hashCode();
	}

	@Override
	public boolean equals(Object metaData) {
		if (metaData == null && !(metaData instanceof TokenMetaData)) {
			return false;
		}
		return this.token.equals(((TokenMetaData) metaData).getToken());
	}

	public long getStudentSessionLogId() {
		return studentSessionLogId;
	}

	public void setStudentSessionLogId(long studentSessionLogId) {
		this.studentSessionLogId = studentSessionLogId;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public Collection<GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Collection<GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

}
