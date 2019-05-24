package com.spring.service;

import java.time.LocalDateTime;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.dao.TicketDao;
import com.spring.exception.ErrorException;
import com.spring.model.DetailTicket;
import com.spring.model.SubDetailTicket;
import com.spring.model.Ticket;

@Service
public class TicketService {
	@Autowired
	TicketDao ticketDao;
	
//===============================*Header Ticket*==================================	
	
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
	
	public void deleteTicket(String id) throws ErrorException {
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
	
	//===============================*Detail Ticket*==================================
	public String insertDetailTicket(DetailTicket detailTicket) throws ErrorException{
		if (ticketDao.isDetailTicketIdExist(detailTicket.getId()) == true) {
			throw new ServiceException("Ticket detail already exist.");
		}
		if (ticketDao.isDetailTicketBkExist(detailTicket.getTicket().getId(), detailTicket.getMessageDate()) == true) {
			throw new ServiceException("Ticket detail id already exist!");
		}
		ticketDao.saveDetailTicket(detailTicket);
		return "New ticket successfully added";
	}
	
	public void updateDetailTicket(DetailTicket detailTicket) throws ErrorException {
		if (ticketDao.isTicketIdExist(detailTicket.getId()) == false) {
			throw new ServiceException("Ticket detail not found!");
		}
		if (ticketDao.isTicketBkExist(detailTicket.getTicket().getId()) == false) {
			throw new ServiceException("Ticket detail not found!");
		}
		if (!detailTicket.getTicket().getId().equals(ticketDao.findDetailTicketById(detailTicket.getId()).getTicket().getId())) {
			throw new ServiceException("Ticket detail header cannot be changed!");
		}

		ticketDao.saveDetailTicket(detailTicket);
	}
	
	public void deleteDetailTicket(String id) throws ErrorException {
		if (ticketDao.isDetailTicketIdExist(id) == true) {
			ticketDao.removeDetailTicket(ticketDao.findDetailTicketById(id));
		} else {
			throw new ErrorException("Image not found!");
		}
	}
	
	public DetailTicket findDetailTicketById(String id) {
		DetailTicket detailTicket = new DetailTicket();

		if (ticketDao.findDetailTicketById(id) != null) {
			detailTicket = ticketDao.findDetailTicketById(id);
			
			return detailTicket;
		} else {
			return detailTicket;
		}
	}

	public DetailTicket findDetailTicketByBk(String ticketId, LocalDateTime messageDate) {
		DetailTicket detailTicket = new DetailTicket();

		if (ticketDao.findDetailTicketByBk(ticketId, messageDate) != null) {
			detailTicket = ticketDao.findDetailTicketByBk(ticketId, messageDate);
			
			return detailTicket;
		} else {
			return detailTicket;
		}
	}
	
	//===============================*Detail Ticket*==================================
	public String insertSubDetailTicket(SubDetailTicket subDetailTicket) throws ErrorException{
		if (ticketDao.isSubDetailTicketIdExist(subDetailTicket.getId()) == true) {
			throw new ServiceException("Ticket already exist.");
		}
		if (ticketDao.isSubDetailTicketBkExist(subDetailTicket.getTicket().getId(), subDetailTicket.getFileName()) == true) {
			throw new ServiceException("Ticket detail id already exist!");
		}
		ticketDao.saveSubDetailTicket(subDetailTicket);
		return "New ticket successfully added";
	}
	
	public void updateSubDetailTicket(SubDetailTicket subDetailTicket) throws ErrorException {
		if (ticketDao.isTicketIdExist(subDetailTicket.getId()) == false) {
			throw new ServiceException("Detail ticket not found!");
		}
		if (ticketDao.isTicketBkExist(subDetailTicket.getTicket().getId()) == false) {
			throw new ServiceException("Detail ticket not found!");
		}
		if (!subDetailTicket.getTicket().getId().equals(ticketDao.findDetailTicketById(subDetailTicket.getId()).getTicket().getId())) {
			throw new ServiceException("Detail ticket cannot be changed!");
		}

		ticketDao.saveSubDetailTicket(subDetailTicket);
	}
}
