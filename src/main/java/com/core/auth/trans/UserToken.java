package com.core.auth.trans;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * The persistent class for the user_token database table.
 * 
 */
@Entity
@Table(name="user_token")
@NamedQuery(name="UserToken.findAll", query="SELECT u FROM UserToken u")
public class UserToken implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id 
	@SequenceGenerator(name = "id_sequence", sequenceName = "id_sequence",allocationSize=5  )
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;

	private boolean active;

	private String token;

	@JsonIgnore
	private long uid;

	@Column(name="user_agent")
	private String userAgent;
	
	@Column(name="valid_till")
	private Date validTill;
	
	@JsonIgnore
	@Column(name="creation_date",updatable=false)
	private Date creationDate;

	public UserToken() {}
	
	@PrePersist
	void prePersistCall() {
		this.creationDate = new Date();
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean getActive() {
		return this.active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Date getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public long getUid() {
		return this.uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public Date getValidTill() {
		return this.validTill;
	}

	public void setValidTill(Date validTill) {
		this.validTill = validTill;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

}