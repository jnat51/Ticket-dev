package com.spring.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="tbl_ticket", uniqueConstraints = @UniqueConstraint(columnNames = {"ticket_code"}))
public class Ticket {
	public enum Status{
		open,close,reopen
	}
	
	@Id
	@Column(name = "id")
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name="UUID", strategy="org.hibernate.id.UUIDGenerator")
	private String id;
	
	@Column(name="ticket_code")
	private String ticketCode;
	
	@OneToOne
	@JoinColumn(name="mapping_id", referencedColumnName="id")
	private Mapping mapping;
	
	@Column(name="ticket_date")
	private Date ticketDate;
	
	@Column(name="title")
	private String title;
	
	@OneToOne
	@JoinColumn(name="customer_id", referencedColumnName="id")
	private Customer customer;
	
	@Enumerated(EnumType.STRING)
	@Column(name="status")
	private Status status;
	
	@OneToMany(mappedBy="ticket", cascade=CascadeType.REMOVE, fetch=FetchType.EAGER)
	private List<DetailTicket> details;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTicketCode() {
		return ticketCode;
	}

	public void setTicketCode(String ticketCode) {
		this.ticketCode = ticketCode;
	}

	public Mapping getMapping() {
		return mapping;
	}

	public void setMapping(Mapping mapping) {
		this.mapping = mapping;
	}

	public Date getTicketDate() {
		return ticketDate;
	}

	public void setTicketDate(Date ticketDate) {
		this.ticketDate = ticketDate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public List<DetailTicket> getDetails() {
		return details;
	}

	public void setDetails(List<DetailTicket> details) {
		this.details = details;
	}
}
