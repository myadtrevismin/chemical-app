package com.core.common;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;

@MappedSuperclass
public abstract class Audit implements Serializable {
	private static final long serialVersionUID = 1L;

	@JsonIgnore
	@JoinColumn(name = "created_by", updatable = false)
	private long createdBy;

	@JsonIgnore
	@JoinColumn(name = "updated_by")
	private long updatedBy;

	@Column(name = "creation_date", updatable = false)
	private Date creationDate;

	@Column(name = "updation_date")
	private Date updationDate;

	public long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(long createdBy) {
		this.createdBy = createdBy;
	}

	public long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(long updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getUpdationDate() {
		return updationDate;
	}

	public void setUpdationDate(Date updationDate) {
		this.updationDate = updationDate;
	}

	@PrePersist
	void prePersistCall() {
		this.creationDate = CommonUtils.getCurrentTimeStamp();
		this.updationDate = this.creationDate;

		if (this.createdBy == 0) {
			this.createdBy = CommonUtils.getUserIdFromSession();
		}

		if (this.updatedBy == 0) {
			this.updatedBy = this.createdBy;
		}
	}

	@PreUpdate
	void preUpdateCall() {
		Long user = CommonUtils.getUserIdFromSession();

		if (user != null) {
			this.updatedBy = user;
		}

		this.updationDate = CommonUtils.getCurrentTimeStamp();
	}
}
