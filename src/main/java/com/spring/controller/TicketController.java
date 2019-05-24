package com.spring.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.service.spi.ServiceException;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.spring.model.DetailTicket;
import com.spring.model.SubDetailTicket;
import com.spring.model.Ticket;
import com.spring.model.UpdateStatus;
import com.spring.service.TicketService;

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

	@PostMapping(value = "/hdr/sub")
	public ResponseEntity<?> insertTicketWithSs(@RequestParam String ticket,
			@RequestParam(name = "ss", required = false) MultipartFile[] ss) {
		try {
			Date date = new Date();
			ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
			Ticket ticketObject = mapper.readValue(ticket, Ticket.class);

			ticketObject.setTicketDate(date);

			System.out.println("insert ticket:");
			System.out.println(ticketObject.getTicketCode());
			System.out.println(date);

			System.out.println(ticketObject.getDetails().size());

			ticketService.insertTicket(ticketObject);

			System.out.print("ss length:");
			System.out.println(ss.length);

			Ticket tick = ticketService.findTicketByBk(ticketObject.getTicketCode());

			if (ticketObject.getDetails().size() > 0) {
				for (DetailTicket dtl : ticketObject.getDetails()) {
					dtl.setTicket(tick);
					ticketService.insertDetailTicket(dtl);
					if (ss.length > 0) {
						for (MultipartFile mf : ss) {
							byte[] data = mf.getBytes();
							SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
							String dateNow = dateFormat.format(date);
							String[] originalName = mf.getOriginalFilename().split("\\.");
							String fileName = originalName[0] + dateNow + "." + originalName[1];
							String mime = mf.getContentType();

							System.out.println(mf.getOriginalFilename());

							SubDetailTicket subDetailTicket = new SubDetailTicket();

							subDetailTicket.setFileName(fileName);
							subDetailTicket.setMime(mime);
							subDetailTicket.setSs(data);
							subDetailTicket.setDetailId(
									ticketService.findDetailTicketByBk(tick.getId(), dtl.getMessageDate()).getId());

							ticketService.insertSubDetailTicket(subDetailTicket);
						}
					}
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
			Ticket ticket = ticketService.findTicketById(id);

			List<DetailTicket> details = ticket.getDetails();
			for (DetailTicket detail : details) {
				List<SubDetailTicket> subs = ticketService.findSubDetailTicketByDetail(detail.getId());
				for (SubDetailTicket sub : subs) {
					ticketService.deleteSubDetailTicket(sub.getId());
				}
			}

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
			for (DetailTicket detail : ticket.getDetails()) {
				detail.setTicket(null);
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
			for (DetailTicket detail : ticket.getDetails()) {
				detail.setTicket(null);
				details.add(detail);
			}

			return new ResponseEntity<>(ticket, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@PatchMapping(value = "/hdr/{id}")
	public ResponseEntity<?> updateStatus(@PathVariable String id, @RequestBody UpdateStatus updateStatus) {
		try {
			Ticket ticket = ticketService.findTicketById(id);

			ticket.setStatus(updateStatus.getStatus());

			ticketService.updateTicket(ticket);
			return new ResponseEntity<>("Status changed to " + updateStatus.getStatus(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	// ======================================*Detail Ticket*===========================================

	@PostMapping(value = "/dtl/")
	public ResponseEntity<?> insertDetailTicket(@RequestParam String detailTicket,
			@RequestParam(name = "ss", required = false) MultipartFile[] ss) {
		try {
			Date date = new Date();
			ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
			DetailTicket detailObject = mapper.readValue(detailTicket, DetailTicket.class);

			System.out.print("ss length:");
			System.out.println(ss.length);

			ticketService.insertDetailTicket(detailObject);
			if (ss.length > 0) {
				for (MultipartFile mf : ss) {
					byte[] data = mf.getBytes();
					SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
					String dateNow = dateFormat.format(date);
					String[] originalName = mf.getOriginalFilename().split("\\.");
					String fileName = originalName[0] + dateNow + "." + originalName[1];
					String mime = mf.getContentType();

					System.out.println(mf.getOriginalFilename());

					SubDetailTicket subDetailTicket = new SubDetailTicket();

					subDetailTicket.setFileName(fileName);
					subDetailTicket.setMime(mime);
					subDetailTicket.setSs(data);
					subDetailTicket.setDetailId(
					ticketService.findDetailTicketByBk(detailObject.getTicket().getId(), detailObject.getMessageDate()).getId());

					ticketService.insertSubDetailTicket(subDetailTicket);
				}
			}

			return new ResponseEntity<>("Insert detail ticket success", HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(value = "/dtl/")
	public ResponseEntity<?> updateDetailTicket(@RequestBody DetailTicket detailTicket) {
		try {
			ticketService.updateDetailTicket(detailTicket);

			return new ResponseEntity<>("Insert detail ticket success", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(value = "/dtl/{id}")
	public ResponseEntity<?> findDetailTicketById(@PathVariable String id) {
		try {
			DetailTicket dtlticket = ticketService.findDetailTicketById(id);

			System.out.println(dtlticket.getMessageDate());

			return new ResponseEntity<>(dtlticket, HttpStatus.OK);
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

			sb.append(date[0] + "-" + date[1] + "-" + date[2] + " " + date[3] + ":" + date[4] + ":" + date[5]);

			System.out.println(sb.toString());

			LocalDateTime dateTime = LocalDateTime.parse(sb.toString(), format);

			DetailTicket detailTicket = ticketService.findDetailTicketByBk(ticketId, dateTime);

			return new ResponseEntity<>(detailTicket, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	// ======================================*Detail Ticket*===========================================
	@GetMapping(value = "/sub/{id}")
	public ResponseEntity<?> findSubDetailTicketById(@PathVariable String id) {
		try {
			SubDetailTicket subDtlticket = ticketService.findSubDetailTicketById(id);

			return new ResponseEntity<>(subDtlticket, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(value = "/sub/{detailId}/{fileName}")
	public ResponseEntity<?> findSubDetailTicketByBk(@PathVariable String detailId, @PathVariable String fileName) {
		try {
			SubDetailTicket subDtlticket = ticketService.findSubDetailTicketByBk(detailId, fileName);

			return new ResponseEntity<>(subDtlticket, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
}
