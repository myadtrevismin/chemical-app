package com.core.role;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import com.core.common.Audit;

/**
 * The persistent class for the user_base_role database table.
 * 
 */
@Entity
@Table(name = "user_base_role")
@NamedQuery(name = "UserBaseRole.findAll", query = "SELECT u FROM UserBaseRole u")
public class UserBaseRole extends Audit {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "id_sequence", sequenceName = "id_sequence", allocationSize = 5)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_sequence")
	@Column(unique = true, nullable = false)
	private Long id;

	@Column(unique = true, nullable = false, updatable = false)
	private String code;

	@Column(unique = true, nullable = false)
	private String name;
	
	private String description;

	@Column(name = "is_default_role")
	private boolean defaultRole;

	// bi-directional many-to-one association to UserBaseRolePermission
	@OneToMany(mappedBy = "role", cascade= {CascadeType.ALL},fetch=FetchType.EAGER)
	private List<UserBaseRolePermission> permissions;

	public UserBaseRole() {
	}

	public UserBaseRole(long roleId) {
		this.id = roleId;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
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

	public String getCode() {
		return this.code;
	}

	public void setCode(String rolecode) {
		this.code = rolecode;
	}

	public List<UserBaseRolePermission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<UserBaseRolePermission> permissions) {
		this.permissions = permissions;
	}

	public boolean isDefaultRole() {
		return defaultRole;
	}

	public void setDefaultRole(boolean defaultRole) {
		this.defaultRole = defaultRole;
	}
}