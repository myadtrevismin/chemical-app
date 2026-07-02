package com.org.purchase;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.core.common.Audit;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.org.company.Company;
import com.querydsl.core.annotations.PropertyType;
import com.querydsl.core.annotations.QueryType;

/**
 * The persistent class for the purchase database table.
 * 
 */
@Entity
@Table(name = "purchase")
@NamedQuery(name = "Purchase.findAll", query = "SELECT s FROM Purchase s")
public class Purchase extends Audit {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "id_sequence", sequenceName = "id_sequence", allocationSize = 5)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_sequence")
	private Long id;

	@Column(name = "contact_name")
	private String contactName;

	@Column(name = "Description")
	private String description;

	private String email;

	@ManyToOne
	@JoinColumn(name = "company")
	private Company company;

	private String code;

	private String status;

	@Column(name = "enquiry_date")
	private Date enquiryDate;

	private String phone;

	private String source;

	private int quantity;

	private double amount;

	@Column(name = "order_id")
	private Long order;

	@Column(name="status_notes")
	private String statusNotes;

	@OneToMany(mappedBy = "reference")
	private Set<PurchaseProduct> products;

	@OneToMany(mappedBy = "reference")
	private Set<PurchaseUser> users;

	@Transient
	@JsonProperty(access = Access.WRITE_ONLY)
	@QueryType(PropertyType.SIMPLE)
	private long uid;
	
	@Column(name="admin_approval")
	private String adminApproval;

	public Purchase() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContactName() {
		return this.contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getOrder() {
		return order;
	}

	public void setOrder(Long order) {
		this.order = order;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getSource() {
		return this.source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Date getEnquiryDate() {
		return enquiryDate;
	}

	public void setEnquiryDate(Date enquiryDate) {
		this.enquiryDate = enquiryDate;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int qty) {
		this.quantity = qty;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Set<PurchaseProduct> getProducts() {
		return products;
	}

	public void setProducts(Set<PurchaseProduct> products) {
		this.products = products;
	}

	public Set<PurchaseUser> getUsers() {
		return users;
	}

	public void setUsers(Set<PurchaseUser> users) {
		this.users = users;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public String getStatusNotes() {
		return statusNotes;
	}

	public void setStatusNotes(String statusNotes) {
		this.statusNotes = statusNotes;
	}

}