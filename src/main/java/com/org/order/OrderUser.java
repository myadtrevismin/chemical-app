package com.org.order;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import com.core.common.Audit;
import com.core.user.entity.User;

/**
 * The persistent class for the oder_user database table.
 * 
 */
@Entity
@Table(name = "order_user")
@NamedQuery(name = "OrderUser.findAll", query = "SELECT s FROM OrderUser s")
public class OrderUser extends Audit {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "id_sequence", sequenceName = "id_sequence", allocationSize = 5)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_sequence")
	private Long id;

	private boolean active = true;

	@ManyToOne
	@JoinColumn(name = "uid")
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "order_id")
	private Order reference;

	public OrderUser() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Order getReference() {
		return reference;
	}

	public void setReference(Order order) {
		this.reference = order;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}