package com.spring.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
import com.spring.enumeration.Enum.Stat;
import com.spring.model.DetailTicket;
import com.spring.model.SubDetailTicket;
import com.spring.model.Ticket;
import com.spring.model.UpdateStatus;
import com.spring.model.admin.Admin;
import com.spring.model.agent.Agent;
import com.spring.model.company.Company;
import com.spring.model.customer.Customer;
import com.spring.service.AdminService;
import com.spring.service.AgentService;
import com.spring.service.CustomerService;
import com.spring.service.TicketService;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Controller
@RestController
@RequestMapping("/ticket")
public class TicketController {
	@Autowired
	TicketService ticketService;
	@Autowired
	CustomerService customerService;
	@Autowired
	AdminService adminService;
	@Autowired
	AgentService agentService;
	@Autowired
	private JavaMailSender javaMailSender;

	// ======================================*Header
	// Ticket*===========================================
//	@PostMapping(value = "/hdr")
//	public ResponseEntity<?> insertTicket(@RequestBody Ticket ticket) {
//		try {
//			Date date = new Date();
//
//			SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
//			int ticketCount = ticketService.findByMonth(Calendar.getInstance().get(Calendar.MONTH)+1).size() + 1;
//			Customer customer = customerService.findCustomerById(ticket.getCustomer().getId());
//
//			String ticketCode = customer.getCompany().getCompanyCode()+ "-" + format.format(date) + "-" + ticketCount;
//			System.out.println(ticketCode);
//			
//			ticket.setTicketCode(ticketCode);
//			ticket.setTicketDate(date);
//
//			System.out.println("insert ticket:");
//			System.out.println(ticket.getTicketCode());
//			System.out.println(ticket.getTicketDate());
//
//			System.out.println(ticket.getDetails().size());
//
//			ticketService.insertTicket(ticket);
//
//			Ticket tick = ticketService.findTicketByBk(ticket.getTicketCode());
//
//			if (ticket.getDetails().size() > 0) {
//				for (DetailTicket dtl : ticket.getDetails()) {
//					dtl.setTicket(tick);
//					ticketService.insertDetailTicket(dtl);
//				}
//			}
//
//			return new ResponseEntity<>("insert success", HttpStatus.CREATED);
//		} catch (Exception e) {
//			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
//		}
//	}

	@PostMapping(value = "/hdr")
	public ResponseEntity<?> insertTicketWithSs(@RequestParam String ticket,
			@RequestParam(name = "ss", required = false) MultipartFile[] ss) {
		try {
			Date date = new Date();
			List<String> emails = new ArrayList<String>();
			
			SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
			int ticketCount = ticketService.findByMonth(Calendar.getInstance().get(Calendar.MONTH)+1).size() + 1;
			
			ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
			Ticket ticketObject = mapper.readValue(ticket, Ticket.class);
			
			Customer customer = customerService.findCustomerById(ticketObject.getCustomer().getId());
			Agent agent = agentService.findById(ticketObject.getAgent().getId());

			ticketObject.setStatus(Stat.open);
			String ticketCode = customer.getCompany().getCompanyCode()+ "-" + format.format(date) + "-" + ticketCount;
			System.out.println(ticketCode);
			
			emails.add(agent.getEmail());
			System.out.println(agent.getEmail());
			emails.add(customer.getEmail());
			System.out.println(customer.getEmail());
			
			for(Admin admin : adminService.findAll())
			{
				emails.add(admin.getEmail());
			}
			
			System.out.println(emails.size());
						
			ticketObject.setTicketCode(ticketCode);
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
			
			int i = 1;
			SimpleMailMessage mail = new SimpleMailMessage();
			// setTo(from, to)
			for(String email : emails) {
				System.out.println("test " + i);
				System.out.println(email);
			mail.setTo("jnat51.jg@gmail.com", email);

			mail.setSubject("New ticket," + ticketCode);
			mail.setText("Ticket with code:" + ticketCode + " has been made.");

			System.out.println("send...");

			javaMailSender.send(mail);

			System.out.println("sent");
			i++;
			}

			return new ResponseEntity<>("insert success", HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(value = "/hdr")
	public ResponseEntity<?> updateTicket(@RequestBody Ticket ticket) {
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
			
			Company company = ticket.getCustomer().getCompany();
			
			List<Customer> customers = new ArrayList<Customer>();
			company.setCustomers(customers);
			
			ticket.getCustomer().setCompany(company);

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
	
	@GetMapping(value = "/hdr/customer/{customerId}/{status}")
	public ResponseEntity<?> findTicketByCustomer(@PathVariable String customerId, @PathVariable String status) {
		try {
			List<Ticket> tickets = ticketService.findByCustomer(customerId, status);

			List<DetailTicket> details = new ArrayList<DetailTicket>();
			Ticket tick = new Ticket();
			for(Ticket ticket : tickets) {
				Company company = ticket.getCustomer().getCompany();
				
				List<Customer> customers = new ArrayList<Customer>();
				company.setCustomers(customers);
				
				ticket.getCustomer().setCompany(company);
				
				for (DetailTicket detail : ticket.getDetails()) {
					detail.setTicket(null);
					details.add(detail);
				}
				
				ticket.setDetails(details);
			}

			return new ResponseEntity<>(tickets, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping(value = "/hdr/status/{status}")
	public ResponseEntity<?> findTicketStatus(@PathVariable String status) {
		try {			
			List<Ticket> tickets = ticketService.findByStatus(status);

			List<DetailTicket> details = new ArrayList<DetailTicket>();
			for(Ticket ticket : tickets) {
				Company company = ticket.getCustomer().getCompany();
				
				List<Customer> customers = new ArrayList<Customer>();
				company.setCustomers(customers);
				
				ticket.getCustomer().setCompany(company);
				
				for (DetailTicket detail : ticket.getDetails()) {
					detail.setTicket(null);
					details.add(detail);
				}
				
				ticket.setDetails(details);
			}

			return new ResponseEntity<>(tickets, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping(value = "/hdr/company/{companyId}/{status}")
	public ResponseEntity<?> findTicketByCompany(@PathVariable String companyId, @PathVariable String status) {
		try {
			List<Ticket> tickets = ticketService.findByCompany(companyId, status);

			List<DetailTicket> details = new ArrayList<DetailTicket>();
			Ticket tick = new Ticket();
			for(Ticket ticket : tickets) {
				Company company = ticket.getCustomer().getCompany();
				
				List<Customer> customers = new ArrayList<Customer>();
				company.setCustomers(customers);
				
				ticket.getCustomer().setCompany(company);
				
				for (DetailTicket detail : ticket.getDetails()) {
					detail.setTicket(null);
					details.add(detail);
				}
				
				ticket.setDetails(details);
			}

			return new ResponseEntity<>(tickets, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping(value = "/hdr/agent/{agentId}/{status}")
	public ResponseEntity<?> findTicketByAgent(@PathVariable String agentId, @PathVariable String status) {
		try {
			List<Ticket> tickets = ticketService.findByAgent(agentId, status);

			List<DetailTicket> details = new ArrayList<DetailTicket>();
			for(Ticket ticket : tickets) {
				Company company = ticket.getCustomer().getCompany();
				
				List<Customer> customers = new ArrayList<Customer>();
				company.setCustomers(customers);
				
				ticket.getCustomer().setCompany(company);
				
				for (DetailTicket detail : ticket.getDetails()) {
					detail.setTicket(null);
					details.add(detail);
				}
				
				ticket.setDetails(details);
			}

			return new ResponseEntity<>(tickets, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(value = "/hdr/code/{ticketCode}")
	public ResponseEntity<?> findTicketByBk(@PathVariable String ticketCode) {
		try {
			Ticket ticket = ticketService.findTicketByBk(ticketCode);
			
			Customer customer = ticket.getCustomer();
			Company company = customer.getCompany();
			
			company.setCustomers(new ArrayList<Customer>());
			
			List<DetailTicket> dtl = new ArrayList<DetailTicket>();
			Ticket tick = new Ticket();
			for(DetailTicket details : ticket.getDetails()) {
				details.setTicket(tick);
				dtl.add(details);
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

			System.out.println(updateStatus.getStatus());
			ticket.setStatus(updateStatus.getStatus());

			ticketService.updateTicket(ticket);
			return new ResponseEntity<>("Status changed to " + updateStatus.getStatus(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	// ======================================*Detail Ticket*===========================================

	@PostMapping(value = "/dtl/{idHeader}")
	public ResponseEntity<?> insertDetailTicket(@RequestParam String detailTicket,
			@RequestParam(name = "ss", required = false) MultipartFile[] ss, @PathVariable String idHeader) {
		try {
			Date date = new Date();
			LocalDateTime messageDate = LocalDateTime.now();
			ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
			DetailTicket detailObject = mapper.readValue(detailTicket, DetailTicket.class);

			System.out.print("ss length:");
			System.out.println(ss.length);
			
			detailObject.setTicket(ticketService.findTicketById(idHeader));
			detailObject.setMessageDate(messageDate);

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
					ticketService.findDetailTicketByBk(idHeader, detailObject.getMessageDate()).getId());

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

	// ======================================*Detail
	// Ticket*===========================================
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
