package com.org.company;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;

import com.core.common.Audit;
import com.core.common.Constants;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity
@Table(name = "company_contact")
public class CompanyContact extends Audit {
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name = "id_sequence", sequenceName = "id_sequence", allocationSize = 5)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_sequence")
	@Column(unique = true, nullable = false)
	private long id;
	
	private String type;
	
	private String name;

	private boolean status;
	
	@Email(message="The field should be in email format")
	@NotNull(message=Constants.NOT_EMPTY)
	
	private String email;
	
	@NotNull(message=Constants.NOT_EMPTY)
	private String phone;
	
	private String department;
	
	@Pattern(regexp="[MFN]",message="Gender should (M)ale, (F)emalte or (N)eutral")
	private String gender;
	
	private String aboutWork;
	
	private String reportsTo;
	
	@Column(name="first_met")
	private String firstMet;
	
	private String whatsapp;
	
	private String wechat;
	
	private String qq;
	
	private String linkedin;
	
	@PastOrPresent(message="Dob should be old date") 
	private Date dob;
	
	@PastOrPresent(message="Anniversary should be old date") 
	private Date anniversary;
	
	private String previousCompany;
	
	private boolean image;
	
	@ManyToOne
	@JsonProperty(access = Access.WRITE_ONLY)
	//@NotNull(message = Constants.NOT_EMPTY)
	@JoinColumn(name = "company")
	private Company company;

	@ManyToOne
	@JsonProperty(access = Access.WRITE_ONLY)
	@JoinColumn(name = "branch")
	private CompanyBranch branch;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAboutWork() {
		return aboutWork;
	}

	public void setAboutWork(String aboutWork) {
		this.aboutWork = aboutWork;
	}

	public String getReportsTo() {
		return reportsTo;
	}

	public void setReportsTo(String reportsTo) {
		this.reportsTo = reportsTo;
	}

	public String getFirstMet() {
		return firstMet;
	}

	public void setFirstMet(String firstMet) {
		this.firstMet = firstMet;
	}

	public String getWhatsapp() {
		return whatsapp;
	}

	public void setWhatsapp(String whatsapp) {
		this.whatsapp = whatsapp;
	}

	public String getWechat() {
		return wechat;
	}

	public void setWechat(String wechat) {
		this.wechat = wechat;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getLinkedin() {
		return linkedin;
	}

	public void setLinkedin(String linkedin) {
		this.linkedin = linkedin;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public Date getAnniversary() {
		return anniversary;
	}

	public void setAnniversary(Date anniversary) {
		this.anniversary = anniversary;
	}

	public String getPreviousCompany() {
		return previousCompany;
	}

	public void setPreviousCompany(String previousCompany) {
		this.previousCompany = previousCompany;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isImage() {
		return image;
	}

	public void setImage(boolean image) {
		this.image = image;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
