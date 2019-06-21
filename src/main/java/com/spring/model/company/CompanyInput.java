package com.spring.model.company;

import java.util.List;

import com.spring.enumeration.Enum.Active;
import com.spring.model.customer.CustomerInput;

public class CompanyInput {
private String id;
	private String companyCode;
	private String companyName;
	private String address;
	private String imageId;
	private Active status;
	private List<CustomerInput> customers;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getImageId() {
		return imageId;
	}
	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
	public Active getStatus() {
		return status;
	}
	public void setStatus(Active status) {
		this.status = status;
	}
	public List<CustomerInput> getCustomers() {
		return customers;
	}
	public void setCustomers(List<CustomerInput> customers) {
		this.customers = customers;
	}
}
