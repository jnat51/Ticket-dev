package com.spring.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class TicketData {
	@Id
	private String id;
	private String companyName;
	private int open;
	private int closed;
	private int reopen;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public int getOpen() {
		return open;
	}
	public void setOpen(int open) {
		this.open = open;
	}
	public int getClosed() {
		return closed;
	}
	public void setClosed(int closed) {
		this.closed = closed;
	}
	public int getReopen() {
		return reopen;
	}
	public void setReopen(int reopen) {
		this.reopen = reopen;
	}
	
}
