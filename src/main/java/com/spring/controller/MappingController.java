package com.spring.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.model.Mapping;
import com.spring.model.MappingReport;
import com.spring.model.customer.Customer;
import com.spring.service.MappingService;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Controller
@RestController
@RequestMapping("/map")
public class MappingController {
	@Autowired
	MappingService mappingService;
	
	@PostMapping("/")
	public ResponseEntity<?> insertMapping(@RequestBody Mapping pic){
		try {
			mappingService.insert(pic);
			
			return new ResponseEntity<>("Map successfuly set", HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(e.getMessage() , HttpStatus.BAD_REQUEST);
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteMapping(@PathVariable String id){
		try {
			mappingService.delete(id);
			
			return new ResponseEntity<>("Pic successfuly deleted", HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(e.getMessage() , HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping("/")
	public ResponseEntity<?> updateMapping(@RequestBody Mapping pic){
		try {
			mappingService.update(pic);
			
			return new ResponseEntity<>("Pic successfuly updated", HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(e.getMessage() , HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> findById(@PathVariable("id") String id) {
		try {
		Mapping pic = mappingService.findById(id);
		
		 List<Customer> customers = new ArrayList<Customer>();
		 for (Customer cust : pic.getCompany().getCustomers()) {
				cust.setCompany(null);
				customers.add(cust);
			}
		 
		 pic.getCompany().setCustomers(customers);
		
		return new ResponseEntity<>(pic , HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage() , HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/company/{companyId}")
	public ResponseEntity<?> findByBk(@PathVariable("companyId") String companyId) {
		try {
		Mapping pic = mappingService.findByBk(companyId);
		
		List<Customer> customers = new ArrayList<Customer>();
		 for (Customer cust : pic.getCompany().getCustomers()) {
				cust.setCompany(null);
				customers.add(cust);
			}
		 
		 pic.getCompany().setCustomers(customers);
		
		return new ResponseEntity<>(pic , HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage() , HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/")
	public ResponseEntity<?> findMapWithLogo() {
		try {
		List<MappingReport> maps = mappingService.findWithCompanyLogo();
		
		return new ResponseEntity<>(maps , HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage() , HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/status/active")public ResponseEntity<?> getAllWithStatus(){
		try {
			List<MappingReport> maps = mappingService.getAllWithStatus();
			
			return new ResponseEntity<>(maps , HttpStatus.OK);
			} catch (Exception e) {
				return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
			}
	}
}
