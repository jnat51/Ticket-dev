package com.spring.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.spring.model.Customer;
import com.spring.model.Image;
import com.spring.service.CompanyService;
import com.spring.service.CustomerService;
import com.spring.service.ImageService;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Controller
@RestController
@Transactional
@RequestMapping("/customer")
public class CustomerController {
	@Autowired
	CompanyService companyService;
	@Autowired
	CustomerService customerService;
	@Autowired
	ImageService imageService;
	
	@PostMapping(value = "/")
	public ResponseEntity<?> insertCustomer(@RequestParam(name="pp",required = false) MultipartFile image, @ModelAttribute Customer customer) {
		try {
			Customer cust = new Customer();
			String pass = customer.getPassword();
			String generatedSecuredPasswordHash = BCrypt.hashpw(pass, BCrypt.gensalt(12));
			
			cust.setEmail(customer.getEmail());
			cust.setUsername(customer.getUsername());
			cust.setPassword(generatedSecuredPasswordHash);
			cust.setName(customer.getName());
			cust.setPosition(customer.getPosition());
			cust.setCompany(companyService.findCompanyById(customer.getCompany().getId()));
			System.out.println(customer.getCompany().getId());
						
			if (image.toString().isEmpty() == false) {
				Image img = new Image();
				byte[] data = image.getBytes();
				
				Date date = new Date();
				SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
				String dateNow = dateFormat.format(date);
				String[] originalName = image.getOriginalFilename().split("\\.");
				String fileName = originalName[0] + dateNow + "." + originalName[1];
				String mime = image.getContentType();
				
				img.setImage(data);
				img.setFileName(fileName);
				img.setMime(mime);
				
				imageService.insert(img);
				System.out.println(imageService.findByBk(fileName, data).getId());
				cust.setImageId(imageService.findByBk(fileName, data).getId());
			}
			
			String msg = customerService.insertCustomer(cust);

			return new ResponseEntity<>(msg, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping(value = "/")
	public ResponseEntity<?> updateCustomer(@RequestParam(name="pp",required = false) MultipartFile image, @ModelAttribute Customer customer) {
		try {
			Customer cust = new Customer();
			String pass = customer.getPassword();
			String generatedSecuredPasswordHash = BCrypt.hashpw(pass, BCrypt.gensalt(12));
			
			cust.setEmail(customer.getEmail());
			cust.setUsername(customer.getUsername());
			cust.setPassword(generatedSecuredPasswordHash);
			cust.setName(customer.getName());
						
			if (image.toString().isEmpty() == false) {
				Image img = new Image();
				byte[] data = image.getBytes();
				
				Date date = new Date();
				SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
				String dateNow = dateFormat.format(date);
				String[] originalName = image.getOriginalFilename().split("\\.");
				String fileName = originalName[0] + dateNow + "." + originalName[1];
				String mime = image.getContentType();
				
				img.setImage(data);
				img.setFileName(fileName);
				img.setMime(mime);
				
				System.out.println(cust.getImageId());
				imageService.delete(cust.getImageId());
				imageService.insert(img);
				cust.setImageId(imageService.findByBk(fileName, data).getId());
			}
			
			String msg = customerService.insertCustomer(customer);

			return new ResponseEntity<>(msg, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> deleteCustomer(@PathVariable String id) {
		try {
			customerService.deleteCustomer(id);
			imageService.delete(customerService.findCustomerById(id).getImageId());

			return new ResponseEntity<>("Company successfully deleted!", HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> getCustomerById(@PathVariable String id){
		try {
			Customer cust = customerService.findCustomerById(id);

			return new ResponseEntity<>(cust, HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@GetMapping(value = "/{companyCode}")
	public ResponseEntity<?> getCompanyByBk(@PathVariable String username){
		try {
			Customer cust = customerService.findCustomerByBk(username);

			return new ResponseEntity<>(cust, HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
}
