package com.core.role;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The persistent class for the user_base_permission database table.
 * 
 */
@Entity
@Table(name = "user_base_permission")
@NamedQuery(name = "UserBasePermission.findAll", query = "SELECT u FROM UserBasePermission u")
public class UserBasePermission implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "id_sequence", sequenceName = "id_sequence", allocationSize = 5)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_sequence")
	@Column(unique = true, nullable = false)
	private Long id;

	private String description;

	@Column(name = "flow_code")
	private String flowCode;

	private String url;

	private String method;

	@Column(name = "is_default_permission")
	private boolean defaultPermission;

	private boolean active;

	@JsonIgnore
	@OneToMany(mappedBy = "permission")
	private List<UserBaseRolePermission> permissions;

	public UserBasePermission() {
	}

	public UserBasePermission(long id) {
		this.id = id;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFlowCode() {
		return this.flowCode;
	}

	public void setFlowCode(String flowcode) {
		this.flowCode = flowcode;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public boolean isDefaultPermission() {
		return defaultPermission;
	}

	public void setDefaultPermission(boolean defaultPermission) {
		this.defaultPermission = defaultPermission;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public List<UserBaseRolePermission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<UserBaseRolePermission> permissions) {
		this.permissions = permissions;
	}
}