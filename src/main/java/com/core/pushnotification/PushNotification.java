package com.core.pushnotification;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.PostPersist;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.core.common.CommonUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The persistent class for the push_notification database table.
 * 
 */
@Entity
@Table(name = "push_notification")
@NamedQuery(name = "PushNotification.findAll", query = "SELECT p FROM PushNotification p")
public class PushNotification implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "id_sequence", sequenceName = "id_sequence", allocationSize = 5)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_sequence")
	private long id;

	private String name;

	private String description;

	private String page;

	@Column(name = "posting_date")
	@DateTimeFormat(pattern = CommonUtils.VALID_DATE_TIME)
	private Date postingDate;

	@Column(name = "creation_date")
	private Date creationDate;

	@Column(name = "updation_date")
	private Date updationDate;

	private String image;

	private boolean active;

	@JsonIgnore
	@Column(name = "created_by")
	private long createdBy;

	@JsonIgnore
	@Column(name = "updated_by")
	private long updatedBy;

	@JsonIgnore
	private boolean posted = true;

	public PushNotification() {
	}

	@PrePersist
	public void prePersist() {
		this.creationDate = new Date();
		this.createdBy = CommonUtils.getUserIdFromSession();
		postPersist();
	}
	
	@PostPersist
	public void postPersist() {
		this.updationDate = new Date();
		this.updatedBy = CommonUtils.getUserIdFromSession();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean isPosted() {
		return posted;
	}

	public void setPosted(boolean posted) {
		this.posted = posted;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Date getPostingDate() {
		return postingDate;
	}

	public void setPostingDate(Date postingDate) {
		this.postingDate = postingDate;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Date getUpdationDate() {
		return updationDate;
	}

	public void setUpdationDate(Date updationDate) {
		this.updationDate = updationDate;
	}

	public long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(long creationBy) {
		this.createdBy = creationBy;
	}

	public long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(long updationBy) {
		this.updatedBy = updationBy;
	}
}