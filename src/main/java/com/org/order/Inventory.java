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
 * The persistent class for the order_inventory database table.
 * 
 */
@Entity
@Table(name = "order_inventory")
@JsonIgnoreProperties(ignoreUnknown = true)
@NamedQuery(name = "Inventory.findAll", query = "SELECT s FROM Inventory s")
public class Inventory extends Audit {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "id_sequence", sequenceName = "id_sequence", allocationSize = 5)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_sequence")
	private Long id;

	@Column(name = "invoice_no")
	private String invoiceNo;

	private String type;

	private String phase;

	@Column(name = "batch_no")
	private String batchNo;

	private String origin;

	private String destination;

	private String eta;

	@Column(name = "last_location")
	private String lastLocation;

	@Column(name = "packaging_type")
	private String packagingType;

	@Column(name = "package_count")
	private int packageCount;

	private String transporter;

	@Column(name = "lr_date")
	private Date lrDate;

	@Column(name = "lr_details")
	private String lrDetails;

	@Column(name = "transportation_charges")
	private double transportationCharges;

	@Column(name = "loading_unloading_charges")
	private double loadingUnloadingCharges;

	@Column(name = "proof_of_delivery")
	private String proofOfDelivery;

	@Column(name = "delivery_status")
	private String status;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "order_id")
	private Order order;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "company")
	private Company company;

	public Inventory() {
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

	public String getPhase() {
		return phase;
	}

	public void setPhase(String phase) {
		this.phase = phase;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getEta() {
		return eta;
	}

	public void setEta(String eta) {
		this.eta = eta;
	}

	public String getLastLocation() {
		return lastLocation;
	}

	public void setLastLocation(String lastLocation) {
		this.lastLocation = lastLocation;
	}

	public String getPackagingType() {
		return packagingType;
	}

	public void setPackagingType(String packagingType) {
		this.packagingType = packagingType;
	}

	public int getPackageCount() {
		return packageCount;
	}

	public void setPackageCount(int packageCount) {
		this.packageCount = packageCount;
	}

	public String getTransporter() {
		return transporter;
	}

	public void setTransporter(String transporter) {
		this.transporter = transporter;
	}

	public Date getLrDate() {
		return lrDate;
	}

	public void setLrDate(Date lrDate) {
		this.lrDate = lrDate;
	}

	public String getLrDetails() {
		return lrDetails;
	}

	public void setLrDetails(String lrDetails) {
		this.lrDetails = lrDetails;
	}

	public double getTransportationCharges() {
		return transportationCharges;
	}

	public void setTransportationCharges(double transportationCharges) {
		this.transportationCharges = transportationCharges;
	}

	public double getLoadingUnloadingCharges() {
		return loadingUnloadingCharges;
	}

	public void setLoadingUnloadingCharges(double loadingUnloadingCharges) {
		this.loadingUnloadingCharges = loadingUnloadingCharges;
	}

	public String getProofOfDelivery() {
		return proofOfDelivery;
	}

	public void setProofOfDelivery(String proofOfDelivery) {
		this.proofOfDelivery = proofOfDelivery;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String deliveryStatus) {
		this.status = deliveryStatus;
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