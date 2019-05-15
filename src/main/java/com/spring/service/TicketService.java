package com.spring.service;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;

import com.spring.dao.TicketDao;
import com.spring.exception.ErrorException;
import com.spring.model.Company;
import com.spring.model.Ticket;

public class TicketService {
	@Autowired
	TicketDao ticketDao;
	
	public String insertTicket(Ticket ticket) throws ErrorException{
		if (ticketDao.isTicketIdExist(ticket.getId()) == true) {
			throw new ServiceException("Ticket already exist.");
		}
		if (ticketDao.isTicketBkExist(ticket.getTicketCode()) == true) {
			throw new ServiceException("Ticket code already exist!");
		}
		ticketDao.saveTicket(ticket);
		return "New ticket successfully added";
	}
	
	public void updateTicket(Ticket ticket) throws ErrorException {
		if (ticketDao.isTicketIdExist(ticket.getId()) == false) {
			throw new ServiceException("Ticket not found!");
		}
		if (ticketDao.isTicketBkExist(ticket.getTicketCode()) == false) {
			throw new ServiceException("Ticket not found!");
		}
		if (!ticket.getTicketCode().equals(ticketDao.findTicketById(ticket.getId()).getTicketCode())) {
			throw new ServiceException("Company code cannot be changed!");
		}

		ticketDao.saveTicket(ticket);
	}
	
	public void removeTicket(String id) throws ErrorException {
		if (ticketDao.isTicketIdExist(id) == true) {
			ticketDao.removeTicket(ticketDao.findTicketById(id));
		} else {
			throw new ErrorException("Image not found!");
		}
	}
	
	public Ticket findTicketById(String id) {
		Ticket ticket = new Ticket();

		if (ticketDao.findTicketById(id) != null) {
			return ticketDao.findTicketById(id);
		} else {
			return ticket;
		}
	}

	public Ticket findTicketByBk(String ticketCode) {
		Ticket ticket = new Ticket();

		if (ticketDao.findTicketByBk(ticketCode) != null) {
			return ticketDao.findTicketByBk(ticketCode);
		} else {
			return ticket;
		}
	}
}
