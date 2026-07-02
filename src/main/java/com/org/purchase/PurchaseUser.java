package com.org.purchase;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.core.common.Audit;
import com.core.user.entity.User;

/**
 * The persistent class for the purchase_user database table.
 * 
 */
@Entity
@Table(name = "purchase_user")
@NamedQuery(name = "PurchaseUser.findAll", query = "SELECT s FROM PurchaseUser s")
public class PurchaseUser extends Audit {
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
	@JoinColumn(name = "enquiry")
	private Purchase reference;

	public PurchaseUser() {
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

	public Purchase getReference() {
		return this.reference;
	}

	public void setReference(Purchase reference) {
		this.reference = reference;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}