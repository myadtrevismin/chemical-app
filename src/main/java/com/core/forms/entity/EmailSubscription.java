package com.core.forms.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

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