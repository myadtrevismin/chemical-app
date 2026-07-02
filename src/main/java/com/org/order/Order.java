package com.org.order;

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
import javax.validation.constraints.NotNull;

import com.core.common.Audit;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.org.company.Company;

/**
 * The persistent class for the order database table.
 * 
 */
@Entity
@Table(name = "orders")
@JsonIgnoreProperties(ignoreUnknown = true)
@NamedQuery(name = "Order.findAll", query = "SELECT s FROM Order s")
public class Order extends Audit {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "id_sequence", sequenceName = "id_sequence", allocationSize = 5)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_sequence")
	private Long id;

	private String code;

	private String type;

	@Column(name = "enquiry_id")
	private long enquiryId;

	private String status;

	private String email;

	private String phone;

	@Column(name = "contact_name")
	private String contactName;

	private String description;

	private String terms;

	private int quantity;

	private double amount;

	@Column(name = "billing_address")
	private String billingAddress;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "company")
	private Company company;

	@OneToMany(mappedBy = "reference")
	private Set<OrderProduct> products;
	
	@Column(name="accounts_approval")
	private String accountsApproval;
	
	@Column(name="inventory_update")
	private String inventoryUpdate;
	

	public Order() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getEnquiryId() {
		return enquiryId;
	}

	public void setEnquiryId(long enquiryId) {
		this.enquiryId = enquiryId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
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

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTerms() {
		return terms;
	}

	public void setTerms(String terms) {
		this.terms = terms;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(String billingAddress) {
		this.billingAddress = billingAddress;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Set<OrderProduct> getProducts() {
		return products;
	}

	public void setProducts(Set<OrderProduct> products) {
		this.products = products;
	}

	public String getAccountsApproval() {
		return accountsApproval;
	}

	public void setAccountsApproval(String accountsApproval) {
		this.accountsApproval = accountsApproval;
	}

	public String getInventoryUpdate() {
		return inventoryUpdate;
	}

	public void setInventoryUpdate(String inventoryUpdate) {
		this.inventoryUpdate = inventoryUpdate;
	}
}