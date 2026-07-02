package com.org.sales;

import java.io.Serializable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import com.org.product.Product;


/**
 * The persistent class for the sales_quotation_product database table.
 * 
 */
@Entity
@Table(name="sales_quotation_product")
@NamedQuery(name="SalesQuotationProduct.findAll", query="SELECT s FROM SalesQuotationProduct s")
public class SalesQuotationProduct implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "id_sequence", sequenceName = "id_sequence", allocationSize = 5)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_sequence")
	private Long id;
	
	private int slno;
	
	private int quantity;
	
	private double amount;
	
	private String status;

	@ManyToOne
	@NotNull
	@JoinColumn(name = "product")
	private Product product;

	//bi-directional many-to-one association to Sales
	@ManyToOne
	@JoinColumn(name="quotation")
	private SalesQuotation reference;

	public SalesQuotationProduct() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getQuantity() {
		return this.quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getSlno() {
		return this.slno;
	}

	public void setSlno(int slno) {
		this.slno = slno;
	}

	public SalesQuotation getReference() {
		return reference;
	}

	public void setReference(SalesQuotation enquiry) {
		this.reference = enquiry;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

}