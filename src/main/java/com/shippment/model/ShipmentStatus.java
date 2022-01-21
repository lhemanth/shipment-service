package com.shippment.model;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ShipmentStatus {

	private String status;
	@JsonIgnore
	private Double value;
	private Set<String> packageType;
	private String cost;
	private Set<String> emailAddress;
	
	public ShipmentStatus(String status, Double value, Set<String> packageType, Set<String> emailAddress) {
		super();
		this.status = status;
		this.value = value;
		this.packageType = packageType;
		this.emailAddress = emailAddress;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Double getValue() {
		return value;
	}
	public void setValue(Double value) {
		this.value = value;
	}
	public Set<String> getPackageType() {
		return packageType;
	}
	public void setPackageType(Set<String> packageType) {
		this.packageType = packageType;
	}

	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

	public Set<String> getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(Set<String> emailAddress) {
		this.emailAddress = emailAddress;
	}
	
}
