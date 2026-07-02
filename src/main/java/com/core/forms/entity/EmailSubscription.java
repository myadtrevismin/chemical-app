package com.core.forms.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * The persistent class for the email_subscription database table.
 * 
 */
@Entity
@Table(name="email_subscription")
@NamedQuery(name="EmailSubscription.findAll", query="SELECT e FROM EmailSubscription e")
public class EmailSubscription implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id 
	@SequenceGenerator(name = "id_sequence", sequenceName = "id_sequence", allocationSize = 5)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_sequence")
	@Column(unique = true, nullable = false)
	private long id;

	private String email;

	@Column(name="creation_date")
	private Date creationDate;
	
	private boolean subscribe;
	
	private String uqstring;
	
	private Long uid;

	public EmailSubscription() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean getSubscribe() {
		return this.subscribe;
	}

	public void setSubscribe(boolean subscribe) {
		this.subscribe = subscribe;
	}

	public String getUqstring() {
		return uqstring;
	}

	public void setUqstring(String uqstring) {
		this.uqstring = uqstring;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
}