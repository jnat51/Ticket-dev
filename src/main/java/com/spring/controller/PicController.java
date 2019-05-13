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

import com.spring.model.Pic;
import com.spring.service.PicService;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Controller
@RestController
@Transactional
@RequestMapping("/pic")
public class PicController {
	@Autowired
	PicService picService;
	
	@PostMapping("/")
	public ResponseEntity<?> insertPic(@RequestBody Pic pic){
		try {
			picService.insert(pic);
			
			return new ResponseEntity<>("Pic successfuly inserted", HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(e.getMessage() , HttpStatus.BAD_REQUEST);
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletePic(@PathVariable String id){
		try {
			picService.delete(id);
			
			return new ResponseEntity<>("Pic successfuly deleted", HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(e.getMessage() , HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping("/")
	public ResponseEntity<?> updatePic(@RequestBody Pic pic){
		try {
			picService.update(pic);
			
			return new ResponseEntity<>("Pic successfuly updated", HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(e.getMessage() , HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> findPicById(@PathVariable("id") String id) {
		try {
		Pic pic = picService.findById(id);
		
		return new ResponseEntity<>(pic , HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage() , HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/company/{companyId}")
	public ResponseEntity<?> findPicByBk(@PathVariable("companyId") String companyId) {
		try {
		Pic pic = picService.findByBk(companyId);
		
		return new ResponseEntity<>(pic , HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage() , HttpStatus.BAD_REQUEST);
		}
	}
}
