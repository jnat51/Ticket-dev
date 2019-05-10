package com.spring.controller;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
	public ResponseEntity<?> insertAgent(@ModelAttribute AgentInsert agent, @RequestParam("pp") MultipartFile file) {
		try {
			Agent ag = new Agent();
			String pass = agent.getPassword();
			String generatedSecuredPasswordHash = BCrypt.hashpw(pass, BCrypt.gensalt(12));

			byte[] data = file.getBytes();

			System.out.println(pass);
			System.out.println(generatedSecuredPasswordHash);

			ag.setEmail(agent.getEmail());
			ag.setUsername(agent.getUsername());
			ag.setPassword(generatedSecuredPasswordHash);
			ag.setName(agent.getName());

			if (file.toString().isEmpty() == false) {
				ag.setPp(data);
			}

			return new ResponseEntity<>(agentService.insert(ag), HttpStatus.CREATED);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	@PutMapping(value = "/")
	public ResponseEntity<?> updateAgent(@ModelAttribute AgentInsert agent, @RequestParam("pp") MultipartFile file) {
		try {
			Agent ag = new Agent();
			String pass = agent.getPassword();
			String generatedSecuredPasswordHash = BCrypt.hashpw(pass, BCrypt.gensalt(12));

			byte[] data = file.getBytes();

			System.out.println(pass);
			System.out.println(generatedSecuredPasswordHash);

			ag.setId(agent.getId());
			ag.setEmail(agent.getEmail());
			ag.setUsername(agent.getUsername());
			ag.setPassword(generatedSecuredPasswordHash);
			ag.setName(agent.getName());

			if (file.toString().isEmpty() == false) {
				ag.setPp(data);
			}
			
			agentService.update(ag);
			
			return new ResponseEntity<>("Update success", HttpStatus.CREATED);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> deleteAgent(@PathVariable String id) {
		try {
			agentService.delete(id);

			return new ResponseEntity<>("Agent successfully deleted!", HttpStatus.OK);
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
