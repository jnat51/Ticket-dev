package com.spring.model.admin;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

import com.spring.enumeration.Enum.Active;

@Entity
@Table(name="update_admin", uniqueConstraints = @UniqueConstraint(columnNames = {"email"}))
public class AdminUpdate {
	@Id
	@Column(name = "id")
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name="UUID", strategy="org.hibernate.id.UUIDGenerator")
	private String id;
	@Column(name="email")
	private String email;
	@Column(name="name")
	private String name;
	@Column(name="image_id")
	private String imageId;
	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private Active active;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImageId() {
		return imageId;
	}
	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
	public Active getActive() {
		return active;
	}
	public void setActive(Active active) {
		this.active = active;
	}
}
