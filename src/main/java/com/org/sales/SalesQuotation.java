package com.org.sales;

import java.util.Date;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.core.common.Audit;
import com.org.company.Company;

/**
 * The persistent class for the sales_quotation database table.
 * 
 */
@Entity
@Table(name = "sales_quotation")
@NamedQuery(name = "SalesQuotation.findAll", query = "SELECT s FROM SalesQuotation s")
public class SalesQuotation extends Audit {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "id_sequence", sequenceName = "id_sequence", allocationSize = 5)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_sequence")
	private Long id;

	private String code;

	@Size(max = 1000)
	private String specification;

	private String make;

	private String packing;

	private double gst;

	@Column(name = "delivery_period")
	private Integer deliveryPeriod;

	private double amount;
	
	private double quantity;

	@Column(name = "transportation_charges")
	private double transportationCharges;

	private Date validTill;

	private String terms;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "enquiry")
	private Sales enquiry;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "company")
	private Company company;

	@OneToMany(mappedBy = "reference")
	private Set<SalesQuotationProduct> products;

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

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public double getGst() {
		return gst;
	}

	public void setGst(double gst) {
		this.gst = gst;
	}

	public Integer getDeliveryPeriod() {
		return deliveryPeriod;
	}

	public void setDeliveryPeriod(Integer deliveryPeriod) {
		this.deliveryPeriod = deliveryPeriod;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public Sales getEnquiry() {
		return enquiry;
	}

	public void setEnquiry(Sales enquiry) {
		this.enquiry = enquiry;
	}

	public double getTransportationCharges() {
		return transportationCharges;
	}

	public void setTransportationCharges(double transportationCharges) {
		this.transportationCharges = transportationCharges;
	}

	public Date getValidTill() {
		return validTill;
	}

	public void setValidTill(Date validTill) {
		this.validTill = validTill;
	}

	public String getTerms() {
		return terms;
	}

	public void setTerms(String temrs) {
		this.terms = temrs;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public String getPacking() {
		return packing;
	}

	public void setPacking(String packing) {
		this.packing = packing;
	}

	public Set<SalesQuotationProduct> getProducts() {
		return products;
	}

	public void setProducts(Set<SalesQuotationProduct> products) {
		this.products = products;
	}

}