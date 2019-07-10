package com.spring.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.dao.TicketDataDao;
import com.spring.model.Ticket;
import com.spring.model.TicketData;

@Service
public class TicketDataService {
	@Autowired
	TicketDataDao ticketDataDao;
	
	public List<TicketData> findTicketData(){
		System.out.println("service");
		List<TicketData> tickets = new ArrayList<TicketData>();
		
		if(ticketDataDao.getTicketData().size() > 0) {
			tickets = ticketDataDao.getTicketData();
			
			return tickets;
		}
		else {
			return tickets;
		}
	}
}
