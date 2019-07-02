package com.spring.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.dao.TicketDao;
import com.spring.exception.ErrorException;
import com.spring.model.DetailTicket;
import com.spring.model.SubDetailTicket;
import com.spring.model.Ticket;
import com.spring.model.TicketImage;
import com.spring.model.company.Company;
import com.spring.model.customer.Customer;

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
		System.out.println("Update ticket");
		if (ticketDao.isTicketIdExist(ticket.getId()) == false) {
			boolean test = ticketDao.isTicketIdExist(ticket.getId());
			System.out.println(test);
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
			throw new ErrorException("Ticket not found!");
		}
	}
	
	public List<Object[]> findByMonth(int month){
		List<Object[]> tickets = new ArrayList<Object[]>();
		
		if(ticketDao.findByMonth(month).size() > 0) {
			tickets = ticketDao.findByMonth(month);
			
			return tickets;
		}
		else {
			return tickets;
		}
	}
	
	public List<Ticket> findByCustomer(String customerId, String status){
		List<Ticket> tickets = new ArrayList<Ticket>();
		
		if(ticketDao.findByCustomer(customerId, status).size() > 0) {
			tickets = ticketDao.findByCustomer(customerId, status);
			
			return tickets;
		}
		else {
			return tickets;
		}
	}
	
	public TicketImage findTicketWithImage(String id) {
		TicketImage ticket = new TicketImage();
		
		if(ticketDao.findTicketWithImage(id) != null) {
			ticket = ticketDao.findTicketWithImage(id);
			
			return ticket;
		}
		else {
			return ticket;
		}
	}
	
	
	public List<Ticket> findByCompany(String companyId, String status){
		List<Ticket> tickets = new ArrayList<Ticket>();
		
		if(ticketDao.findByCompany(companyId, status).size() > 0) {
			tickets = ticketDao.findByCompany(companyId, status);
			
			return tickets;
		}
		else {
			return tickets;
		}
	}
	
	public List<Ticket> findByAgent(String agentId, String status){
		List<Ticket> tickets = new ArrayList<Ticket>();
		
		if(ticketDao.findByAgent(agentId, status).size() > 0) {
			tickets = ticketDao.findByAgent(agentId, status);
			
			return tickets;
		}
		else {
			return tickets;
		}
	}
	
	public List<Ticket> findByStatus(String ticketStatus){
		List<Ticket> tickets = new ArrayList<Ticket>();
		
		if(ticketDao.findByStatus(ticketStatus).size() > 0) {
			tickets = ticketDao.findByStatus(ticketStatus);
			
			return tickets;
		}
		else {
			return tickets;
		}
	}
	
	public Ticket findTicketById(String id) {
		Ticket ticket = new Ticket();
		if(ticketDao.findTicketById(id) != null) {
			ticket = ticketDao.findTicketById(id);
			
			return ticket;
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
		if (ticketDao.isDetailTicketIdExist(detailTicket.getId()) == false) {
			throw new ServiceException("Ticket detail not found!");
		}
		if (ticketDao.isDetailTicketBkExist(detailTicket.getTicket().getId(), detailTicket.getMessageDate()) == false) {
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
			throw new ErrorException("Detail ticket");
		}
	}
	
	public DetailTicket findDetailTicketById(String id) {
		DetailTicket detailTicket = new DetailTicket();

		if (ticketDao.findDetailTicketById(id) != null) {
			detailTicket = ticketDao.findDetailTicketById(id);
			
			List<DetailTicket> details = new ArrayList<DetailTicket>();
			
			Ticket ticket = detailTicket.getTicket();
			ticket.setDetails(details);
			
			Customer customer = ticket.getCustomer();
			Company company = customer.getCompany();
			company.setCustomers(new ArrayList<Customer>());
			
			detailTicket.setTicket(ticket);
			
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
		if (ticketDao.isSubDetailTicketBkExist(subDetailTicket.getDetailId(), subDetailTicket.getFileName()) == true) {
			throw new ServiceException("Ticket detail id already exist!");
		}
		ticketDao.saveSubDetailTicket(subDetailTicket);
		return "New ticket successfully added";
	}
	
	public void updateSubDetailTicket(SubDetailTicket subDetailTicket) throws ErrorException {
		if (ticketDao.isTicketIdExist(subDetailTicket.getId()) == false) {
			throw new ServiceException("Sub detail ticket not found!");
		}
		if (ticketDao.isTicketBkExist(subDetailTicket.getDetailId()) == false) {
			throw new ServiceException("Sub detail ticket not found!");
		}
		if (!subDetailTicket.getDetailId().equals(ticketDao.findSubDetailTicketById(subDetailTicket.getId()).getDetailId())) {
			throw new ServiceException("Sub detail ticket header cannot be changed!");
		}

		ticketDao.saveSubDetailTicket(subDetailTicket);
	}
	
	public void deleteSubDetailTicket(String id) throws ErrorException {
		if (ticketDao.isSubDetailTicketIdExist(id) == true) {
			ticketDao.removeSubDetailTicket(ticketDao.findSubDetailTicketById(id));
		} else {
			throw new ErrorException("Sub detail ticket not found!");
		}
	}
	
	public SubDetailTicket findSubDetailTicketById(String id) throws ErrorException{
		SubDetailTicket subDetailTicket = new SubDetailTicket();

		if (ticketDao.findDetailTicketById(id) != null) {
			subDetailTicket = ticketDao.findSubDetailTicketById(id);
			
			return subDetailTicket;
		} else {
			return subDetailTicket;
		}
	}
	
	public SubDetailTicket findSubDetailTicketByBk(String detailId, String fileName) throws ErrorException{
		SubDetailTicket subDetailTicket = new SubDetailTicket();

		if (ticketDao.findSubDetailTicketByBk(detailId, fileName) != null) {
			subDetailTicket = ticketDao.findSubDetailTicketByBk(detailId, fileName);
			
			return subDetailTicket;
		} else {
			return subDetailTicket;
		}
	}
	
	public List<SubDetailTicket> findSubDetailTicketByDetail(String detailId) throws ErrorException {
		List<SubDetailTicket> subDetailTicket = new ArrayList<SubDetailTicket>();
		
		if (ticketDao.findSubDetailTicketByDetail(detailId).size() > 0) {
			subDetailTicket = ticketDao.findSubDetailTicketByDetail(detailId);
			
			return subDetailTicket;
		} else {
			return subDetailTicket;
		}
	}
}
