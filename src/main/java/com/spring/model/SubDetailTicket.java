//package com.spring.model;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.Id;
//import javax.persistence.Table;
//import javax.persistence.UniqueConstraint;
//
//import org.hibernate.annotations.GenericGenerator;
//
//@Entity
//@Table(name="tbl_ticket", uniqueConstraints = @UniqueConstraint(columnNames = {"ticket_code"}))
//public class SubDetailTicket {
//	@Id
//	@Column(name = "id")
//	@GeneratedValue(generator = "UUID")
//	@GenericGenerator(name="UUID", strategy="org.hibernate.id.UUIDGenerator")
//	private String id;
//	
//	
//}
