package com.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.model.Agent;
import com.spring.service.AgentService;

@CrossOrigin(origins = "*", allowedHeaders= "*")
@Controller
@RestController
@RequestMapping("/agent")
public class AgentController {
	@Autowired
	AgentService agentService;
	
	@Transactional
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> getBarangbyId(@PathVariable("id") String id)
	{	
		try {
		Agent barang = agentService.findById(id);
		
		return new ResponseEntity<>(barang, HttpStatus.OK);
		}
		catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
}
