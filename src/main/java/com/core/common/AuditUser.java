package com.core.common;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import com.core.user.entity.HUser;
import com.fasterxml.jackson.annotation.JsonIgnore;

@MappedSuperclass
public abstract class AuditUser implements Serializable {
	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "created_by", updatable = false)
	private HUser createdBy;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "updated_by")
	private HUser updatedBy;

	@Column(name = "creation_date", updatable = false)
	private Date creationDate;

	@Column(name = "updation_date")
	private Date updationDate;

	public HUser getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(HUser createdBy) {
		this.createdBy = createdBy;
	}

	public HUser getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(HUser updatedBy) {
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

		if (this.createdBy == null) {
			this.createdBy = CommonUtils.getHUserFromSession();
		}

		if (this.updatedBy == null) {
			this.updatedBy = this.createdBy;
		}
	}

	@PreUpdate
	void preUpdateCall() {
		this.updatedBy = CommonUtils.getHUserFromSession();
		this.updationDate = CommonUtils.getCurrentTimeStamp();
	}
}
