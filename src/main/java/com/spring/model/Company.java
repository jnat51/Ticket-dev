package com.spring.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "tbl_company", uniqueConstraints = @UniqueConstraint(columnNames = {"company_code"}))
public class Company {
	@Id
	@Column(name = "id")
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name="UUID", strategy="org.hibernate.id.UUIDGenerator")
	private String id;
	
	@Column(name = "company_code")
	private String companyCode;

	@Column(name = "company_name")
	private String companyName;

	@Column(name = "address")
	private String address;
	
	@Column(name = "image_id")
	private String imageId;
	
	@OneToMany(mappedBy="company", cascade=CascadeType.REMOVE, fetch=FetchType.EAGER)
	private List<Customer> customers;

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


	public List<Customer> getCustomers() {
		return customers;
	}


	public void setCustomers(List<Customer> customers) {
		this.customers = customers;
	}
}
