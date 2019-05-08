package com.spring.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.model.Agent;
import com.spring.model.AgentInsert;
import com.spring.service.AgentService;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Controller
@RestController
@Transactional
@RequestMapping("/agent")
public class AgentController {
	@Autowired
	AgentService agentService;

	@GetMapping(value = "/{id}")
	public ResponseEntity<?> getAgentbyId(@PathVariable("id") String id) {
		try {
			Agent barang = agentService.findById(id);

			return new ResponseEntity<>(barang, HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	@PostMapping(value = "/")
	public ResponseEntity<?> insertAgent(@RequestBody AgentInsert agentInsert) {
		try {
			Agent ag = new Agent();
			String pass = agentInsert.getPassword();
			String generatedSecuredPasswordHash = BCrypt.hashpw(pass, BCrypt.gensalt(12));
			
			System.out.println(pass);
			System.out.println(generatedSecuredPasswordHash);

			ag.setEmail(agentInsert.getEmail());
			ag.setUsername(agentInsert.getUsername());
			ag.setPassword(generatedSecuredPasswordHash);
			ag.setName(agentInsert.getName());

			return new ResponseEntity<>(agentService.insert(ag), HttpStatus.CREATED);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	@PutMapping(value = "/")
	public ResponseEntity<?> updateAgent(@RequestBody Agent agent) {
		try {
			agentService.update(agent);

			return new ResponseEntity<>("Agent successfully updated!", HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	@GetMapping(value = "/login")
	public ResponseEntity<?> login(@RequestBody Agent agent) {
		try {
			System.out.println(agentService.findUsername(agent.getUsername()).getPassword());
			boolean matched = BCrypt.checkpw(agent.getPassword(),
					agentService.findUsername(agent.getUsername()).getPassword());
			System.out.println(matched);

			return new ResponseEntity<>(matched, HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
}
