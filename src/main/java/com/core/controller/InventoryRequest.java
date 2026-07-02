package com.core.controller;

public class InventoryRequest {
	private Long prodId;
	private Long salesEnquiryId;
	 
	public Long getSalesEnquiryId() {
		return salesEnquiryId;
	}
	public void setSalesEnquiryId(Long salesEnquiryId) {
		this.salesEnquiryId = salesEnquiryId;
	}
	public Long getProdId() {
		return prodId;
	}
	public void setProdId(Long prodId) {
		this.prodId = prodId;
	}
	
	
}
