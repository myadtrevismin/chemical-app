package com.core.role;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

/**
 * The persistent class for the user_base_role_permission database table.
 * 
 */
@Entity
@Table(name = "user_base_role_permission")
@NamedQuery(name = "UserBaseRolePermission.findAll", query = "SELECT u FROM UserBaseRolePermission u")
public class UserBaseRolePermission implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "id_sequence", sequenceName = "id_sequence", allocationSize = 5)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_sequence")
	@Column(unique = true, nullable = false)
	private Long id;

	// bi-directional many-to-one association to UserBasePermission
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "permission")
	private UserBasePermission permission;

	private boolean selected;
	
	
	// bi-directional many-to-one association to UserBaseRole
	@ManyToOne
	@JsonProperty(access = Access.WRITE_ONLY)
	@JoinColumn(name = "role")
	private UserBaseRole role;

	public UserBaseRolePermission() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserBasePermission getPermission() {
		return this.permission;
	}

	public void setPermission(UserBasePermission userBasePermission) {
		this.permission = userBasePermission;
	}

	public UserBaseRole getRole() {
		return this.role;
	}

	public void setRole(UserBaseRole userBaseRole) {
		this.role = userBaseRole;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}