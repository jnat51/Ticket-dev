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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.model.Mapping;
import com.spring.service.MappingService;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Controller
@RestController
@Transactional
@RequestMapping("/map")
public class MappingController {
	@Autowired
	MappingService mappingService;
	
	@PostMapping("/")
	public ResponseEntity<?> insertMapping(@RequestBody Mapping pic){
		try {
			mappingService.insert(pic);
			
			return new ResponseEntity<>("Pic successfuly inserted", HttpStatus.OK);
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
		
		return new ResponseEntity<>(pic , HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage() , HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/company/{companyId}")
	public ResponseEntity<?> findByBk(@PathVariable("companyId") String companyId) {
		try {
		Mapping pic = mappingService.findByBk(companyId);
		
		return new ResponseEntity<>(pic , HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage() , HttpStatus.BAD_REQUEST);
		}
	}
}
