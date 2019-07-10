package com.spring.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.dao.CompanyDao;
import com.spring.dao.TicketDataDao;
import com.spring.model.TicketCount;
import com.spring.model.TicketData;
import com.spring.model.company.Company;

@Service
public class TicketDataService {
	@Autowired
	TicketDataDao ticketDataDao;
	@Autowired
	CompanyDao companyDao;
	
	public List<TicketData> findTicketData(){
		System.out.println("service");
		List<TicketData> tickets = new ArrayList<TicketData>();
		
		for(Company comp:companyDao.findAll())
		{
			System.out.println(comp.getId());
			
			TicketData ticketData = new TicketData();
			ticketData = ticketDataDao.getTicketData(comp.getId());
			
			if(ticketData.getId() != null) {
			tickets.add(ticketData);
			}
		}
		
		if(tickets.size() > 0) {
			return tickets;
		}
		else {
			return tickets;
		}
	}
	
	public TicketData test(){
		System.out.println("service");
		
		TicketData ticket = new TicketData();
		ticket = ticketDataDao.test("f8360b62-5fa0-4c3e-8a48-56568f635607");
		if(!ticket.getId().isEmpty())
		{
			return ticket;
		}
		else {
			return ticket;
		}
	}
	
	public TicketCount countAll(){
		System.out.println("service");
		
		TicketCount ticketCount = new TicketCount();
		ticketCount = ticketDataDao.countAll();
		
		return ticketCount;
	}
}
