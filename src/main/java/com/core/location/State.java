package com.core.location;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name="state")
public class State {
	@Id
	private Long id;
	
	private String name;
	
	@PrePersist
	public void prePersist() throws Exception{
		throw new Exception();
	}
	@PreUpdate
	public void preSave() throws Exception{
		throw new Exception();
	}

	public Long getId() {
		return id;
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
	
	
}
