package com.org.followup;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.core.common.Audit;

/**
 * The persistent class for the followup database table.
 * 
 */
@Entity
@Table(name = "followup")
@NamedQuery(name = "Followup.findAll", query = "SELECT s FROM Followup s")
public class Followup extends Audit {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "id_sequence", sequenceName = "id_sequence", allocationSize = 5)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_sequence")
	private Long id;
	
	private String contact;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "follow_up_date")
	private Date followUpDate;

	private String type;

	private String response;
	
	private String stage;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "next_followup_date")
	private Date nextFollowUpDate;
	
	@Column(name = "next_followup_type")
	private String nextFollowupType;
	
	private String repository = "followups";
	
	private Long reference = 0l;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public Date getFollowUpDate() {
		return followUpDate;
	}

	public void setFollowUpDate(Date followUpDate) {
		this.followUpDate = followUpDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getStage() {
		return stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}

	public Date getNextFollowUpDate() {
		return nextFollowUpDate;
	}

	public void setNextFollowUpDate(Date nextFollowUpDate) {
		this.nextFollowUpDate = nextFollowUpDate;
	}

	public String getNextFollowupType() {
		return nextFollowupType;
	}

	public void setNextFollowupType(String nextFollowupType) {
		this.nextFollowupType = nextFollowupType;
	}

	public String getRepository() {
		return repository;
	}

	public void setRepository(String repository) {
		this.repository = repository;
	}

	public Long getReference() {
		return reference;
	}

	public void setReference(Long reference) {
		this.reference = reference;
	}

}