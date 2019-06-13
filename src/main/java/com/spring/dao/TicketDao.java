package com.spring.dao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.spring.enumeration.Enum.Stat;
import com.spring.model.Company;
import com.spring.model.Customer;
import com.spring.model.DetailTicket;
import com.spring.model.SubDetailTicket;
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
			
			return ticket;
		} catch (Exception e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> findByMonth (int month) {
		try {
			String query = "SELECT * FROM tbl_ticket WHERE EXTRACT(MONTH FROM tbl_ticket.ticket_date) = :month";
			
			List<Object[]> tickets = this.entityManager.createNativeQuery(query)
					.setParameter("month", month)
					.getResultList();
			
			return tickets;
		}catch (Exception e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Ticket> findByCustomer (String customerId) {
		try {
			String query = "FROM Ticket WHERE customer.id = :customerId";
			
			List<Ticket> tickets = this.entityManager.createQuery(query)
					.setParameter("customerId", customerId)
					.getResultList();
			
			return tickets;
		}catch (Exception e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Ticket> findByCompany (String companyId) {
		try {
			String query = "FROM Ticket WHERE customer.company = :companyId";
			
			List<Ticket> tickets = this.entityManager.createQuery(query)
					.setParameter("companyId", companyId)
					.getResultList();
			
			return tickets;
		}catch (Exception e) {
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
	
	@SuppressWarnings("unchecked")
	public List<Ticket> findByStatus (String status) {
		try {
			String query = "FROM Ticket WHERE status = :status";
			
			List<Ticket> tickets = new ArrayList<Ticket>();
			
			tickets = this.entityManager.createNativeQuery(query)
					.setParameter("status", status)
					.getResultList();
			
			if(status.equals("open")) {
			tickets = this.entityManager.createQuery(query)
					.setParameter("status", Stat.open)
					.getResultList();
			}
			else if(status.equals("reopen")) {
			tickets = this.entityManager.createQuery(query)
					.setParameter("status", Stat.reopen)
					.getResultList();
			}
			else if(status.equals("close")) {
			tickets = this.entityManager.createQuery(query)
					.setParameter("status", Stat.close)
					.getResultList();
			}
			
			return tickets;
		}catch (Exception e) {
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
	public void saveDetailTicket(DetailTicket detailTicket) {
		super.entityManager.merge(detailTicket);
	}
	
	public void removeDetailTicket(DetailTicket detailTicket) {
		super.entityManager.remove(detailTicket);
	}
	
	public DetailTicket findDetailTicketById (String id) {
		try {
			String query = "from DetailTicket where id = :id";

			DetailTicket detailTicket = (DetailTicket) this.entityManager.createQuery(query)
					.setParameter("id", id)
					.getSingleResult();

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
	
	//====================================*Sub Detail Ticket*========================================
	public void saveSubDetailTicket(SubDetailTicket subDetailTicket) {
		super.entityManager.merge(subDetailTicket);
	}
	
	public void removeSubDetailTicket(SubDetailTicket subDetailTicket) {
		super.entityManager.remove(subDetailTicket);
	} 	
	
	public SubDetailTicket findSubDetailTicketById (String id) {
		try {
			String query = "from SubDetailTicket where id = :id";

			SubDetailTicket subDetailTicket = (SubDetailTicket) this.entityManager.createQuery(query)
					.setParameter("id", id)
					.getSingleResult();

			return subDetailTicket;
		} catch (Exception e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<SubDetailTicket> findSubDetailTicketByDetail (String detailId) {
		try {
			String query = "from SubDetailTicket where detailId = :detailid";

			List<SubDetailTicket> subDetailTicket = this.entityManager.createQuery(query)
					.setParameter("detailid", detailId)
					.getResultList();

			return subDetailTicket;
		} catch (Exception e) {
			return null;
		}
	}
	
	public SubDetailTicket findSubDetailTicketByBk (String detailId, String fileName) {
		try {
			String query = "FROM SubDetailTicket WHERE detailId = :detailid AND fileName = :filename";

			SubDetailTicket subDetailTicket = (SubDetailTicket) this.entityManager.createQuery(query)
					.setParameter("detailid", detailId)
					.setParameter("filename", fileName)
					.getSingleResult();

			return subDetailTicket;
		} catch (Exception e) {
			return null;
		}
	}
	
	public boolean isSubDetailTicketIdExist(String id)
	{
		if(findSubDetailTicketById(id) == null)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	public boolean isSubDetailTicketBkExist(String detailId, String fileName)
	{
		if(findSubDetailTicketByBk(detailId, fileName) == null)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
}
