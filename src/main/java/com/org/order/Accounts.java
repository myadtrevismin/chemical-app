package com.org.order;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.core.common.Audit;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.org.company.Company;

/**
 * The persistent class for the order_accounts database table.
 * 
 */
@Entity
@Table(name = "order_accounts")
@JsonIgnoreProperties(ignoreUnknown = true)
@NamedQuery(name = "Accounts.findAll", query = "SELECT s FROM Accounts s")
public class Accounts extends Audit {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "id_sequence", sequenceName = "id_sequence", allocationSize = 5)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_sequence")
	private Long id;

	@Column(name = "invoice_no")
	private String invoiceNo;

	private String type;

	@Column(name = "bank_name")
	private String bankName;

	@Column(name = "account_no")
	private long accountNo;

	@Column(name = "payment_type")
	private String paymentType;

	@Column(name = "reference_no")
	private String referenceNo;

	@Column(name = "payment_term")
	private String paymentTerm;

	@Column(name = "amount_paid")
	private double amountPaid;

	@Column(name = "due_date")
	private Date dueDate;

	@Column(name = "payment_status")
	private String status;

	private String description;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "order_id")
	private Order order;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "company")
	private Company company;

	public Accounts() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public long getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(long accountNo) {
		this.accountNo = accountNo;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getReferenceNo() {
		return referenceNo;
	}

	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}

	public String getPaymentTerm() {
		return paymentTerm;
	}

	public void setPaymentTerm(String paymentTerm) {
		this.paymentTerm = paymentTerm;
	}

	public double getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(double amountPaid) {
		this.amountPaid = amountPaid;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String paymentStatus) {
		this.status = paymentStatus;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}
}