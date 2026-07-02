package com.core.user.entity;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.core.common.PreventAnyUpdate;

@Entity
@Table(name = "user")
@EntityListeners(PreventAnyUpdate.class)
@NamedQuery(name = "HUser.findAll", query = "SELECT u FROM HUser u")
public class HUser {
	@Id
	private long id;

	private String name;

	private String email;

	public HUser() {
	}

	public HUser(long id, String name, String email) {
		this.id = id;
		this.name = name;
		this.email = email;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}