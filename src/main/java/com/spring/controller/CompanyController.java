package com.spring.controller;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.spring.model.Company;
import com.spring.model.Customer;
import com.spring.service.CompanyService;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Controller
@RestController
@Transactional
@RequestMapping("/company")
public class CompanyController {
	@Autowired
	CompanyService companyService;
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> deleteCompany(@PathVariable String id) {
		try {
			companyService.deleteCompany(id);

			return new ResponseEntity<>("Company successfully deleted!", HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@PostMapping(value = "/test")
	public ResponseEntity<?> insertCompany(@RequestParam(required = false) MultipartFile companyLogo, @RequestBody Company company) {
		try {
			MultipartFile a = companyLogo;
			byte[] data = a.getBytes();
						
			if (companyLogo.toString().isEmpty() == false) {
				company.setCompanyLogo(data);
			}
			
			for(Customer cust: company.getCustomers())
			{
				cust.setCompany(companyService.findCompanyById(company.getId()));
				companyService.insertCustomer(cust);
			}

			return new ResponseEntity<>(companyService.insertCompany(company), HttpStatus.CREATED);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> getCompanyById(@PathVariable String id){
		try {
			Company comp = companyService.findCompanyById(id);

			return new ResponseEntity<>(comp, HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@GetMapping(value = "/{companyCode}")
	public ResponseEntity<?> getCompanyByBk(@PathVariable String companyCode){
		try {
			Company comp = companyService.findCompanyByBk(companyCode);

			return new ResponseEntity<>(comp, HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
}
