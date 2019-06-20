package com.spring.model.customer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

import com.spring.enumeration.Enum.Active;
import com.spring.model.company.Company;

@Entity
@Table(name = "tbl_customer", uniqueConstraints = @UniqueConstraint(columnNames = {"email"}))
public class Customer {
	@Id
	@Column(name = "id")
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name="UUID", strategy="org.hibernate.id.UUIDGenerator")
	private String id;

	@ManyToOne
	@JoinColumn(name = "company_id", referencedColumnName = "id")
	private Company company;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "email")
	private String email;
	

	@Column(name = "position")
	private String position;
	
	@Column(name = "image_id")
	private String imageId;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "status")
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

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
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
}
