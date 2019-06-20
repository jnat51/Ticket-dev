package com.spring.controller;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
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

import com.spring.enumeration.Enum.Active;
import com.spring.enumeration.Enum.Role;
import com.spring.model.Image;
import com.spring.model.Status;
import com.spring.model.User;
import com.spring.model.admin.AdminAgentInput;
import com.spring.model.agent.AgentUpdate;
import com.spring.service.AgentUpdateService;
import com.spring.service.ImageService;
import com.spring.service.UserService;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Controller
@RestController
@RequestMapping("/xagent")
public class AgentUpdateController {
	@Autowired
	AgentUpdateService agentUpdateService;
	@Autowired
	ImageService imageService;
	@Autowired
	UserService userService;
	@Autowired
	JavaMailSender javaMailSender;
	
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

	@PostMapping(value = "/")
	public ResponseEntity<?> insertAgent(@ModelAttribute AdminAgentInput adminAgentInput,
			@RequestParam(name = "pp", required = false) MultipartFile pp) {
		try {
			AgentUpdate agentUpdate = new AgentUpdate();
			User user = new User();

			agentUpdate.setStatus(Active.active);
			agentUpdate.setEmail(adminAgentInput.getEmail());
			agentUpdate.setName(adminAgentInput.getName());

			agentUpdateService.insert(agentUpdate);

			String pass = passwordGenerator();
			String generatedSecuredPasswordHash = BCrypt.hashpw(pass, BCrypt.gensalt(12));

			user.setUsername(adminAgentInput.getUsername());
			user.setPassword(generatedSecuredPasswordHash);
			user.setRole(Role.agent);
			user.setUser(agentUpdateService.findByBk(agentUpdate.getEmail()).getId());

			userService.insert(user);

			if (pp != null) {
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

				AgentUpdate agent = agentUpdateService.findByBk(agentUpdate.getEmail());

				imageService.insert(img);
				agent.setImageId(imageService.findByBk(fileName, data).getId());
				agentUpdateService.update(agent);
			}

			SimpleMailMessage email = new SimpleMailMessage();
			// setTo(from, to)
			email.setTo("jnat51.jg@gmail.com", adminAgentInput.getEmail());

			email.setSubject("Welcome " + adminAgentInput.getName() + ", New Agent!");
			email.setText("Here is your username and password to login to your account.\nUsername: "
					+ adminAgentInput.getUsername() + "\nPassword: " + pass);

			System.out.println("send...");

			javaMailSender.send(email);

			System.out.println("sent");

			return new ResponseEntity<>("Admin successfuly inserted", HttpStatus.CREATED);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@PutMapping(value = "/")
	public ResponseEntity<?> updateAdmin(@ModelAttribute AgentUpdate agentUpdate,
			@RequestParam(name = "pp", required = false) MultipartFile pp) {
		try {
			agentUpdateService.update(agentUpdate);

			return new ResponseEntity<>("Admin successfuly updated", HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@PatchMapping(value = "/{id}")
	public ResponseEntity<?> patchImage(@PathVariable String id, @RequestParam MultipartFile pp){
		try {
			AgentUpdate agentUpdate = agentUpdateService.findById(id);
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
			
			if (agentUpdate.getImageId() == null) {
			imageService.delete(agentUpdate.getImageId());
			}
			imageService.insert(img);
			
			agentUpdate.setImageId(imageService.findByBk(fileName, data).getId());
			agentUpdateService.update(agentUpdate);
			
			return new ResponseEntity<>("Profile picture updated", HttpStatus.OK);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@PatchMapping(value = "/status/{id}")
	public ResponseEntity<?> updateStatus(@PathVariable String id, @RequestBody Status status) {
		try {
			AgentUpdate agentUpdate = agentUpdateService.findById(id);
			
			agentUpdate.setStatus(status.getStatus());

			agentUpdateService.update(agentUpdate);
			return new ResponseEntity<>("Status changed to " + status.getStatus(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
}
