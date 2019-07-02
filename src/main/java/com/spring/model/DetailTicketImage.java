package com.spring.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;

public class DetailTicketImage {
	private String id;
	private String sender;
	private String message;
	private String messageDate;
	private List<SubDetailTicket> ss;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getMessageDate() {
		return messageDate;
	}
	public void setMessageDate(String messageDate) {
		this.messageDate = messageDate;
	}
	public List<SubDetailTicket> getSs() {
		return ss;
	}
	public void setSs(List<SubDetailTicket> ss) {
		this.ss = ss;
	}
}
