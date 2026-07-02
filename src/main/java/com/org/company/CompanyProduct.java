package com.org.company;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;

import com.core.common.Constants;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.org.product.Product;


/**
 * The persistent class for the company_product database table.
 * 
 */
@Entity
@Table(name="company_product")
@NamedQuery(name="CompanyProduct.findAll", query="SELECT s FROM CompanyProduct s")
public class CompanyProduct implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "id_sequence", sequenceName = "id_sequence", allocationSize = 5)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_sequence")
	private Long id;
	
	@NotNull
	private String type;

	@ManyToOne
	@JsonProperty(access = Access.WRITE_ONLY)
	@NotNull(message = Constants.NOT_EMPTY)
	@JoinColumn(name = "company")
	private Company company;

	@ManyToOne
	@JsonProperty(access = Access.WRITE_ONLY)
	@NotNull(message = Constants.NOT_EMPTY)
	@JoinColumn(name = "product")
	private Product product;
	
	@Transient
	@JsonProperty(access = Access.WRITE_ONLY)
	private boolean delete;
	
	public CompanyProduct() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Company getCompany() {
		return company;
	}

	public Product getProduct() {
		return product;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public boolean isDelete() {
		return delete;
	}

	public void setDelete(boolean delete) {
		this.delete = delete;
	}

}