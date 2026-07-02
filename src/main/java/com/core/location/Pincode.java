package com.core.location;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

@Entity
@Table(name = "pincode")
public class Pincode implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	private long id;
	
	private String pincode;

	private String city;

	private String district;

	private String state;

	@PrePersist
	public void prePersist() throws Exception {
		throw new Exception();
	}

	@PreUpdate
	public void preSave() throws Exception {
		throw new Exception();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}
