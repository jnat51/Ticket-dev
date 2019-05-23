package com.spring.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.model.DetailTicket;
import com.spring.model.Ticket;
import com.spring.model.UpdateStatus;
import com.spring.service.TicketService;

import com.spring.enumeration.Status.Stat;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Controller
@RestController
@RequestMapping("/ticket")
public class TicketController {
	@Autowired
	TicketService ticketService;

	// ======================================*Header Ticket*===========================================
	@PostMapping(value = "/hdr")
	public ResponseEntity<?> insertTicket(@RequestBody Ticket ticket) {
		try {
			Date date = new Date();
						
			ticket.setTicketDate(date);
			
			System.out.println("insert ticket:");
			System.out.println(ticket.getTicketCode());
			System.out.println(ticket.getTicketDate());
			
			System.out.println(ticket.getDetails().size()); 
			
			ticketService.insertTicket(ticket);
			
			
			
			Ticket tick = ticketService.findTicketByBk(ticket.getTicketCode());
			
			if (ticket.getDetails().size() > 0) {
				for (DetailTicket dtl : ticket.getDetails()) {
					dtl.setTicket(tick);
					ticketService.insertDetailTicket(dtl);
				}
			}
			

			return new ResponseEntity<>("insert success", HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(value = "/hdr")
	public ResponseEntity<?> updateTicket(Ticket ticket) {
		try {
			ticketService.updateTicket(ticket);

			return new ResponseEntity<>("Ticket successfully updated", HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping(value = "/hdr/{id}")
	public ResponseEntity<?> deleteTicket(@PathVariable String id) {
		try {
			ticketService.deleteTicket(id);

			return new ResponseEntity<>("Ticket successfully deleted", HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(value = "/hdr/{id}")
	public ResponseEntity<?> findTicketById(@PathVariable String id) {
		try {
			Ticket ticket = ticketService.findTicketById(id);
			
			List<DetailTicket> details = new ArrayList<DetailTicket>();
			Ticket tick = new Ticket();
			for(DetailTicket detail: ticket.getDetails()) {
				detail.setTicket(tick);
				details.add(detail);
			}
			
			ticket.setDetails(details);

			return new ResponseEntity<>(ticket, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(value = "/hdr/code/{ticketCode}")
	public ResponseEntity<?> findTicketByBk(@PathVariable String ticketCode) {
		try {
			Ticket ticket = ticketService.findTicketByBk(ticketCode);
			
			List<DetailTicket> details = new ArrayList<DetailTicket>();
			Ticket tick = new Ticket();
			for(DetailTicket detail: ticket.getDetails()) {
				detail.setTicket(tick);
				details.add(detail);
			}

			return new ResponseEntity<>(ticket, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PatchMapping(value = "/hdr/{id}")
	public ResponseEntity<?> updateStatus(@PathVariable String id,@RequestBody UpdateStatus updateStatus) {
		try {
			Ticket ticket = ticketService.findTicketById(id);
			
			ticket.setStatus(updateStatus.getStatus());
			
			ticketService.updateTicket(ticket);
			return new ResponseEntity<>("Status changed to " + updateStatus.getStatus(), HttpStatus.OK);
		} catch(Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	// ======================================*Detail Ticket*===========================================
	@GetMapping(value = "/dtl/{id}")
	public ResponseEntity<?> findDetailTicketById(@PathVariable String id) {
		try {
			DetailTicket dtlticket = ticketService.findDetailTicketById(id);

			return new ResponseEntity<>(dtlticket, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping(value = "/dtl/")
	public ResponseEntity<?> insertDetailTicket(@RequestBody DetailTicket detailTicket){
		try {
			ticketService.insertDetailTicket(detailTicket);
			
			return new ResponseEntity<>("Insert detail ticket success", HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping(value = "/dtl/")
	public ResponseEntity<?> updateDetailTicket(@RequestBody DetailTicket detailTicket){
		try {
			ticketService.updateDetailTicket(detailTicket);
			
			return new ResponseEntity<>("Insert detail ticket success", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(value = "/dtl/{ticketId}/{messageDate}")
	public ResponseEntity<?> findDetailTicketByBk(@PathVariable String ticketId, @PathVariable String messageDate) {
		try {
			DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			StringBuilder sb = new StringBuilder();
			String[] date = messageDate.split("\\-");
			
			System.out.println(date.length);
			
			sb.append(date[0] + "-" + date[1] + "-" + date[2]+ " " + date[3] +":"+date[4]+":"+date[5]);
			
			System.out.println(sb.toString());
			
			LocalDateTime dateTime = LocalDateTime.parse(sb.toString(), format);
			
			DetailTicket detailTicket = ticketService.findDetailTicketByBk(ticketId,dateTime);
			
			List<DetailTicket> details = new ArrayList<DetailTicket>();

			Ticket ticket = detailTicket.getTicket();
			ticket.setDetails(details);
			
			detailTicket.setTicket(ticket);

			return new ResponseEntity<>(detailTicket, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

}
