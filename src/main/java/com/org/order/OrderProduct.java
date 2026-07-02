package com.org.order;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.org.product.Product;


/**
 * The persistent class for the order_product database table.
 * 
 */
@Entity
@Table(name="order_product")
@NamedQuery(name="OrderProduct.findAll", query="SELECT s FROM OrderProduct s")
public class OrderProduct implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "id_sequence", sequenceName = "id_sequence", allocationSize = 5)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_sequence")
	private Long id;
	
	private int slno;
	
	private int quantity;
	
	private double amount;

	@ManyToOne
	@NotNull
	@JoinColumn(name = "product")
	private Product product;

	//bi-directional many-to-one association to Order
	@ManyToOne
	@JoinColumn(name="order_id")
	private Order reference;
	
	private String uom;
	

	public OrderProduct() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Order getReference() {
		return reference;
	}

	public void setReference(Order order) {
		this.reference = order;
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

	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}

}