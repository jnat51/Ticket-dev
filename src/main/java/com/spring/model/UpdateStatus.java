package com.spring.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.spring.enumeration.Enum.Stat;

@Entity
@Table(name = "tbl_ticket", uniqueConstraints = @UniqueConstraint(columnNames = { "ticket_code"}))
public class UpdateStatus {
	@Id
	private String id;
	
	private Stat status;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Stat getStatus() {
		return status;
	}

	public void setStatus(Stat status) {
		this.status = status;
	}
	
	
}
