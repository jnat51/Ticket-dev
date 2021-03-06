package com.spring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.model.AllCount;
import com.spring.model.ChartData;
import com.spring.model.TicketCount;
import com.spring.model.TicketData;
import com.spring.service.TicketDataService;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Controller
@RestController
@RequestMapping("/ticketdata")
public class TicketDataController {
	@Autowired
	TicketDataService ticketDataService;
	
	@GetMapping(value="/")
	public ResponseEntity<?> findTicketData(){
		try {
			ChartData chartData = new ChartData();
			List<TicketData> tickets = ticketDataService.findTicketData();
			
			for(TicketData ticket : tickets) {
				System.out.println(ticket.getCompanyName());
			}
			
			chartData.setTicketData(tickets);
			
			return new ResponseEntity<>(chartData, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping(value="/test")
	public ResponseEntity<?> test(){
		try {
			TicketData ticket = ticketDataService.test();
			
			return new ResponseEntity<>(ticket, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping(value="/all")
	public ResponseEntity<?> countAll(){
		try {
			AllCount allCount = new AllCount();
			TicketCount ticketCount = ticketDataService.countAll();
			
			allCount.setTicketCount(ticketCount);
			
			return new ResponseEntity<>(allCount, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
}
