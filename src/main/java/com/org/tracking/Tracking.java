package com.org.tracking;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import com.core.common.Audit;
import com.org.company.Company;
import com.org.product.Product;


/**
 * The persistent class for the tracking database table.
 * 
 */
@Entity
@Table(name="tracking")
@NamedQuery(name="Tracking.findAll", query="SELECT s FROM Tracking s")
public class Tracking extends Audit {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "id_sequence", sequenceName = "id_sequence", allocationSize = 5)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_sequence")
	private Long id;

	@Column(name="assigned_to")
	private String assignedTo;

	private String remarks;

	@Column(name="docket_no")
	private String docketNo;

	@Column(name="courier_company")
	private String courierCompany;
	
	@Column(name="dispatch_date")
	private Date dispatchDate;
	
	@Column(name="received_date")
	private Date receivedDate;
	
	@ManyToOne
	@JoinColumn(name = "company")
	private Company company;
	
	@ManyToOne
	@NotNull
	@JoinColumn(name = "product")
	private Product product;
	
	public Tracking() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getDocketNo() {
		return docketNo;
	}

	public void setDocketNo(String docketNo) {
		this.docketNo = docketNo;
	}

	public String getCourierCompany() {
		return courierCompany;
	}

	public void setCourierCompany(String courierCompany) {
		this.courierCompany = courierCompany;
	}

	public Date getDispatchDate() {
		return dispatchDate;
	}

	public void setDispatchDate(Date dispatchDate) {
		this.dispatchDate = dispatchDate;
	}

	public Date getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
}