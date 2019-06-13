package com.spring.controller;

import java.io.IOException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.spring.enumeration.Enum.Active;
import com.spring.model.Company;
import com.spring.model.Image;
import com.spring.model.Mapping;
import com.spring.model.Status;
import com.spring.model.customer.Customer;
import com.spring.service.CompanyService;
import com.spring.service.CustomerService;
import com.spring.service.ImageService;
import com.spring.service.MappingService;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Controller
@RestController
@RequestMapping("/company")
public class CompanyController {
	@Autowired
	CompanyService companyService;
	@Autowired
	CustomerService customerService;
	@Autowired
	ImageService imageService;
	@Autowired
	MappingService mappingService;
	@Autowired
	private JavaMailSender javaMailSender;
	
	public String passwordGenerator() {
		Random RANDOM = new SecureRandom();
		String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		int passwordLength = 8;

		StringBuilder returnValue = new StringBuilder(passwordLength);

		for (int i = 0; i < passwordLength; i++) {
			returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
		}

		return returnValue.toString();
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> deleteCompany(@PathVariable String id) {
		try {
			Company company = companyService.findCompanyById(id);
			
			companyService.deleteCompany(id);
			
			if (company.getImageId() != null) {
			imageService.delete(company.getImageId());
			}

			return new ResponseEntity<>("Company successfully deleted!", HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

//	@PostMapping(value = "/")
//	public ResponseEntity<?> insertCompany(@RequestParam(name = "logo", required = false) MultipartFile logo,
//			@ModelAttribute Company company) {
//		try {
//			if (logo != null) {
//				Image img = new Image();
//				byte[] data = logo.getBytes();
//				
//				Date date = new Date();
//				SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
//				String dateNow = dateFormat.format(date);
//				String[] originalName = logo.getOriginalFilename().split("\\.");
//				String fileName = originalName[0] + dateNow + "." + originalName[1];
//				String mime = logo.getContentType();
//				
//				img.setImage(data);
//				img.setFileName(fileName);
//				img.setMime(mime);
//				
//				imageService.insert(img);
//				company.setImageId(imageService.findByBk(fileName, data).getId());
//			}
//
//			String msg = companyService.insertCompany(company);
//
//			return new ResponseEntity<>(msg, HttpStatus.CREATED);
//		} catch (Exception e) {
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//		}
//	}
	
	@PostMapping(value = "/")
	public ResponseEntity<?> insertNewCompany(@RequestParam String company,
			@RequestParam(name = "logo", required = false) MultipartFile logo) throws JsonParseException, JsonMappingException, IOException
	{
		ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
		Company companyObject = mapper.readValue(company, Company.class);
		companyObject.setStatus(Active.active);
		
		try {
			if (logo != null) {
				Image img = new Image();
				byte[] data = logo.getBytes();
				
				
				Date date = new Date();
				SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
				String dateNow = dateFormat.format(date);
				String[] originalName = logo.getOriginalFilename().split("\\.");
				String fileName = originalName[0] + dateNow + "." + originalName[1];
				String mime = logo.getContentType();
				
				img.setImage(data);
				img.setFileName(fileName);
				img.setMime(mime);
				
				imageService.insert(img);
				companyObject.setImageId(imageService.findByBk(fileName, data).getId());
			}
			
			String msg = companyService.insertCompany(companyObject);
			
			for(Customer customer : companyObject.getCustomers()) {
				String pass = passwordGenerator();
				String generatedSecuredPasswordHash = BCrypt.hashpw(pass, BCrypt.gensalt(12));
				
				customer.setPassword(generatedSecuredPasswordHash);
				customer.setCompany(companyService.findCompanyByBk(companyObject.getCompanyCode()));
				
				customerService.insertCustomer(customer);
				
				SimpleMailMessage email = new SimpleMailMessage();
				// setTo(from, to)
				email.setTo("jnat51.jg@gmail.com", customer.getEmail());

				email.setSubject("Welcome to Linov Support, " + customer.getName() + "!");
				email.setText("Here is your username and password to login to your account.\nUsername: "
						+ customer.getUsername() + "\nPassword: " + pass);

				System.out.println("send...");

				javaMailSender.send(email);
			}
			Company comp = new Company();
			comp.setId(companyService.findCompanyByBk(companyObject.getCompanyCode()).getId());
			Mapping mapping = new Mapping();
			mapping.setCompany(comp);
			
			mappingService.insert(mapping);

			return new ResponseEntity<>(msg, HttpStatus.CREATED);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	@PutMapping(value = "/")
	public ResponseEntity<?> updateCompany(@RequestParam(name = "logo", required = false) MultipartFile logo,
			@ModelAttribute Company company) {
		try {
			Company comp = companyService.findCompanyById(company.getId());
			
			comp.setCompanyName(company.getCompanyName());
			comp.setCompanyCode(company.getCompanyCode());
			comp.setAddress(company.getAddress());
			
			if (logo != null) {
				Image image = new Image();
				
				byte[] data = logo.getBytes();
				String fileName = logo.getOriginalFilename();
				image.setImage(data);
				image.setFileName(fileName);
				image.setMime(logo.getContentType());
				
				imageService.delete(comp.getImageId());
				imageService.insert(image);
				comp.setImageId(imageService.findByBk(fileName, data).getId());
			}

			companyService.updateCompany(comp);

			return new ResponseEntity<>("Company successfully updated", HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@PatchMapping(value = "/status/{id}")
	public ResponseEntity<?> updateStatus(@PathVariable String id, @RequestBody Status status) {
		try {
			Company company= companyService.findCompanyById(id);

			company.setStatus(status.getStatus());

			companyService.updateCompany(company);
			return new ResponseEntity<>("Status changed to " + status.getStatus(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PatchMapping(value = "/image/{id}")
	public ResponseEntity<?> patchImage(@PathVariable String id, @RequestParam MultipartFile pp){
		try {
			Company company = companyService.findCompanyById(id);
			Image img = new Image();
			
			byte[] data = pp.getBytes();
			Date date = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
			String dateNow = dateFormat.format(date);
			String[] originalName = pp.getOriginalFilename().split("\\.");
			String fileName = originalName[0] + dateNow + "." + originalName[1];
			String mime = pp.getContentType();
			
			img.setImage(data);
			img.setFileName(fileName);
			img.setMime(mime);
			
			if(company.getImageId()!= null) {
			imageService.delete(company.getImageId());
			}
			imageService.insert(img);
			
			company.setImageId(imageService.findByBk(fileName, data).getId());
			companyService.updateCompany(company);
			
			return new ResponseEntity<>("Profile picture updated", HttpStatus.OK);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<?> getCompanyById(@PathVariable String id) {
		try {
			Company company = companyService.findCompanyById(id);
			
			List<Customer> customers = new ArrayList<Customer>();
			Company comp = new Company();
			for (Customer cust : company.getCustomers()) {
				cust.setCompany(comp);
				customers.add(cust);
			}
			
			company.setCustomers(customers);

			return new ResponseEntity<>(company, HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@GetMapping(value = "/")
	public ResponseEntity<?> getAllCompany() {
		try {
			System.out.println("find all");
			
			List<Company> company = companyService.findAll();
			
			System.out.println(company.size());
			
			Company comp = new Company();
			
			for (Company co : company) {
				List<Customer> customers = new ArrayList<Customer>();
				for (Customer cust : co.getCustomers()) {
					cust.setCompany(comp);
					customers.add(cust);
				}
				co.setCustomers(customers);
			}

			return new ResponseEntity<>(company, HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	@GetMapping(value = "/code/{companyCode}")
	public ResponseEntity<?> getCompanyByBk(@PathVariable String companyCode) {
		try {
			Company company = companyService.findCompanyByBk(companyCode);
			
			List<Customer> customers = new ArrayList<Customer>();
			Company comp = new Company();
			for (Customer cust : company.getCustomers()) {
				cust.setCompany(comp);
				customers.add(cust);
			}
			
			company.setCustomers(customers);

			return new ResponseEntity<>(company, HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@GetMapping(value = "/status/{status}")
	public ResponseEntity<?> getAllCompanyWithStatus(@PathVariable String status) {
		try {			
			List<Company> company = companyService.findWithStatus(status);
			System.out.println(status);
			
			System.out.println(company.size());
			
			Company comp = new Company();
			
			for (Company co : company) {
				List<Customer> customers = new ArrayList<Customer>();
				for (Customer cust : co.getCustomers()) {
					cust.setCompany(comp);
					customers.add(cust);
				}
				co.setCustomers(customers);
			}

			return new ResponseEntity<>(company, HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
}
