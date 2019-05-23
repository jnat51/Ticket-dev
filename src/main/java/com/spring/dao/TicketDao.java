package com.spring.dao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.spring.model.Company;
import com.spring.model.Customer;
import com.spring.model.DetailTicket;
import com.spring.model.Ticket;

@Repository
@Transactional
public class TicketDao extends ParentDao{
	//====================================*Header Ticket*========================================
	public void saveTicket(Ticket ticket) {
		super.entityManager.merge(ticket);
	}
	
	public void removeTicket(Ticket ticket) {
		super.entityManager.remove(ticket);
	}
	
	public Ticket findTicketById (String id) {
		try {
			String query = "from Ticket where id = :id";

			Ticket ticket = (Ticket) this.entityManager.createQuery(query)
					.setParameter("id", id)
					.getSingleResult();
			
			Customer customer = ticket.getCustomer();
			Company company = customer.getCompany();
			
			company.setCustomers(new ArrayList<Customer>());
			
			List<DetailTicket> dtl = new ArrayList<DetailTicket>();
			Ticket tick = new Ticket();
			for(DetailTicket details : ticket.getDetails()) {
				details.setTicket(tick);
				dtl.add(details);
			}
			
			super.entityManager.clear();
			
			return ticket;
		} catch (Exception e) {
			return null;
		}
	}
	
	public Ticket findTicketByBk (String ticketCode) {
		try {
			String query = "from Ticket where ticketCode = :ticketcode";

			Ticket ticket = (Ticket) this.entityManager.createQuery(query)
					.setParameter("ticketcode", ticketCode)
					.getSingleResult();

			return ticket;
		} catch (Exception e) {
			return null;
		}
	}
	
	public boolean isTicketIdExist(String id)
	{
		if(findTicketById(id) == null)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	public boolean isTicketBkExist(String ticketCode)
	{
		if(findTicketByBk(ticketCode) == null)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	//====================================*Detail Ticket*========================================
	public void saveDetailTicket(DetailTicket DetailTicket) {
		super.entityManager.merge(DetailTicket);
	}
	
	public void removeDetailTicket(DetailTicket DetailTicket) {
		super.entityManager.remove(DetailTicket);
	}
	
	public DetailTicket findDetailTicketById (String id) {
		try {
			String query = "from DetailTicket where id = :id";

			DetailTicket detailTicket = (DetailTicket) this.entityManager.createQuery(query)
					.setParameter("id", id)
					.getSingleResult();
			
			List<DetailTicket> details = new ArrayList<DetailTicket>();
			
			Ticket ticket = detailTicket.getTicket();
			ticket.setDetails(details);
			
			Customer customer = ticket.getCustomer();
			Company company = customer.getCompany();
			company.setCustomers(new ArrayList<Customer>());
			
			detailTicket.setTicket(ticket);
			
			super.entityManager.clear();

			return detailTicket;
		} catch (Exception e) {
			return null;
		}
	}
	
	public DetailTicket findDetailTicketByBk (String ticketId, LocalDateTime messageDate) {
		try {
			String query = "from DetailTicket where ticket.id = :ticketid AND messageDate = :messagedate";

			DetailTicket detailTicket = (DetailTicket) this.entityManager.createQuery(query)
					.setParameter("ticketid", ticketId)
					.setParameter("messagedate", messageDate)
					.getSingleResult();

			return detailTicket;
		} catch (Exception e) {
			return null;
		}
	}
	
	public boolean isDetailTicketIdExist(String id)
	{
		if(findDetailTicketById(id) == null)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	public boolean isDetailTicketBkExist(String ticketId, LocalDateTime messageDate)
	{
		if(findDetailTicketByBk(ticketId, messageDate) == null)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
}
