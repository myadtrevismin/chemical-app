package com.org.company;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.core.common.Audit;

@Entity
@Table(name = "company")
public class Company extends Audit {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "id_sequence", sequenceName = "id_sequence", allocationSize = 5)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_sequence")
	@Column(unique = true, nullable = false)
	private long id;

	@NotBlank(message = "Code cannot be null or empty")
	private String code;

	@NotBlank(message = "Name cannot be null or empty")
	private String name;

	@Pattern(regexp = "[BV]", message = "Company type should be (B)uyer or (V)endor")
	private String type;

	@Pattern(regexp = "[NI]", message = "Company type should be (N)ational or (I)nternational")
	private String locationType;

	@Email
	private String email;

	private String phone;

	private String categories;

	private String contact;
	
	
	private String country;
	
	private String province;
	
	private String city;
	
	private String zipcode;
	
	private String pincode;
	
	

	@Pattern(regexp = "[YN]", message = "Agent Y or N")
	private String agent;

	@Column(name = "interested_categories")
	private String categoriesInterested;

	private String customerType;

	private String rating;

	private String paymentTerms;

	private double turnOver;

	private String gstin;

	private String pan;

	private String fssai;

	private String drugLicense;

	private String others;
	
	private String organizations;
	
	@Column(name="established_date")
	private Date dateOfIncorporation;

	@Pattern(regexp = "[YN]", message = "Agent Y or N")
	private String msme;
	
	@Column(name="msme_id")
	private String msmeId;

	private boolean image;

	@OneToMany(mappedBy = "company")
	private List<CompanyProduct> products;

	@OneToMany(mappedBy = "company")
	private List<CompanyBranch> branches;

	@OneToMany(mappedBy = "company")
	private List<CompanyContact> contacts;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLocationType() {
		return locationType;
	}

	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCategories() {
		return categories;
	}

	public void setCategories(String categories) {
		this.categories = categories;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public String getCategoriesInterested() {
		return categoriesInterested;
	}

	public void setCategoriesInterested(String categoriesInterested) {
		this.categoriesInterested = categoriesInterested;
	}

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getPaymentTerms() {
		return paymentTerms;
	}

	public void setPaymentTerms(String paymentTerms) {
		this.paymentTerms = paymentTerms;
	}

	public double getTurnOver() {
		return turnOver;
	}

	public void setTurnOver(double turnOver) {
		this.turnOver = turnOver;
	}

	public String getGstin() {
		return gstin;
	}

	public void setGstin(String gstin) {
		this.gstin = gstin;
	}

	public String getPan() {
		return pan;
	}

	public void setPan(String pan) {
		this.pan = pan;
	}

	public String getFssai() {
		return fssai;
	}

	public void setFssai(String fssai) {
		this.fssai = fssai;
	}

	public String getOthers() {
		return others;
	}

	public void setOthers(String others) {
		this.others = others;
	}

	public String isMsme() {
		return msme;
	}

	public void setMsme(String msme) {
		this.msme = msme;
	}

	public String getMsme() {
		return msme;
	}

	public List<CompanyContact> getContacts() {
		return contacts;
	}

	public void setContacts(List<CompanyContact> companyContact) {
		this.contacts = companyContact;
	}

	public void setBranches(List<CompanyBranch> companyBranch) {
		this.branches = companyBranch;
	}

	public List<CompanyBranch> getBranches() {
		return branches;
	}

	public String getDrugLicense() {
		return drugLicense;
	}

	public void setDrugLicense(String drugLicense) {
		this.drugLicense = drugLicense;
	}

	public boolean isImage() {
		return image;
	}

	public void setImage(boolean image) {
		this.image = image;
	}

	public String getOrganizations() {
		return organizations;
	}

	public void setOrganizations(String organizations) {
		this.organizations = organizations;
	}

	public List<CompanyProduct> getProducts() {
		return products;
	}

	public void setProducts(List<CompanyProduct> products) {
		this.products = products;
	}

	public String getMsmeId() {
		return msmeId;
	}

	public void setMsmeId(String msmeId) {
		this.msmeId = msmeId;
	}

	public Date getDateOfIncorporation() {
		return dateOfIncorporation;
	}

	public void setDateOfIncorporation(Date dateOfIncorporation) {
		this.dateOfIncorporation = dateOfIncorporation;
	}

}
