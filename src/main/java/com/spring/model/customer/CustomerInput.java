package com.spring.model.customer;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.spring.enumeration.Enum.Active;
import com.spring.model.company.Company;

public class CustomerInput {
	private String id;
	private Company company;
	private String name;
	private String email;
	private String username;
	private String position;
	private Active status;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Company getCompany() {
		return company;
	}
	public void setCompany(Company company) {
		this.company = company;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public Active getStatus() {
		return status;
	}
	public void setStatus(Active status) {
		this.status = status;
	}
}
