package com.org.company;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import com.core.common.Constants;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity
@Table(name = "company_branch_contact")
public class CompanyBranchContact implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "id_sequence", sequenceName = "id_sequence", allocationSize = 5)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_sequence")
	@Column(unique = true, nullable = false)
	private Long id;

	@Column(name="department_name")
	private String departmentName;
	
	@Column(name="phone")
	private String phone;
	
	@Column(name="email")
	@Email(message="should be in email format")
	private String email;
	
	
	@ManyToOne
	@JsonProperty(access = Access.WRITE_ONLY)
	@NotNull(message = Constants.NOT_EMPTY)
	@JoinColumn(name = "company")
	private Company company;

	@ManyToOne
	@JsonProperty(access = Access.WRITE_ONLY)
	@NotNull(message = Constants.NOT_EMPTY)
	@JoinColumn(name = "branch")
	private CompanyBranch branch;
	
	@Transient
	@JsonProperty(access = Access.WRITE_ONLY)
	private boolean delete;

	public Long getId() {
		return id;	
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public String getPhone() {
		return phone;
	}

	public String getEmail() {
		return email;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public CompanyBranch getBranch() {
		return branch;
	}

	public void setBranch(CompanyBranch branch) {
		this.branch = branch;
	}

	public boolean isDelete() {
		return delete;
	}

	public void setDelete(boolean delete) {
		this.delete = delete;
	}
}
