package com.core.controller;

public class InventoryDetails {

	private String batch;
	
	private Long parent;
	
	private Long id;
	
	
	private long availableQty;
	
	private long currentQty;

	public String getBatch() {
		return batch;
	}

	public void setBatch(String batch) {
		this.batch = batch;
	}

	public Long getParent() {
		return parent;
	}

	public void setParent(Long parent) {
		this.parent = parent;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getAvailableQty() {
		return availableQty;
	}

	public void setAvailableQty(long availableQty) {
		this.availableQty = availableQty;
	}

	public long getCurrentQty() {
		return currentQty;
	}

	public void setCurrentQty(long currentQty) {
		this.currentQty = currentQty;
	}
	
	
}
