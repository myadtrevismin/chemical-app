package com.org.approval;

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
@Table(name = "approval")
@NamedQuery(name = "Approval.findAll", query = "SELECT s FROM Approval s")
public class Approval extends Audit {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "id_sequence", sequenceName = "id_sequence", allocationSize = 5)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_sequence")
	private Long id;
 
 
	private String description;
	
 
	private String response;
	   
	private String repository ;
	
	private Long reference = 0l;

	private Date responseDate;
	
	private String status;
	
	private String orderApproval;
	
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	 
	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
 
	public Date getResponseDate() {
		return responseDate;
	}

	public void setResponseDate(Date responseDate) {
		this.responseDate = responseDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOrderApproval() {
		return orderApproval;
	}

	public void setOrderApproval(String orderApproval) {
		this.orderApproval = orderApproval;
	}

}