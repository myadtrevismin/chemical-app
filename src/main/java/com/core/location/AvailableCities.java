package com.core.location;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name="available_cities")
public class AvailableCities {
	@Id
	private Long id;
	
	private String name;
	
	private boolean active;

	public Long getId() {
		return id;
	}

	@PrePersist
	public void prePersist() throws Exception{
		throw new Exception();
	}
	@PreUpdate
	public void preSave() throws Exception{
		throw new Exception();
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	
}
