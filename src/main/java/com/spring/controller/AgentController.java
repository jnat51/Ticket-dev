package com.spring.controller;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
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

import com.spring.enumeration.Enum.Active;
import com.spring.enumeration.Enum.Role;
import com.spring.model.Image;
import com.spring.model.Status;
import com.spring.model.UpdatePassword;
import com.spring.model.User;
import com.spring.model.admin.AdminAgentInput;
import com.spring.model.agent.Agent;
import com.spring.model.agent.AgentLogin;
import com.spring.service.AgentService;
import com.spring.service.ImageService;
import com.spring.service.UserService;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Controller
@RestController
@RequestMapping("/agent")
public class AgentController {
	@Autowired
	AgentService agentService;
	@Autowired
	ImageService imageService;
	@Autowired
	UserService userService;
	@Autowired
    private JavaMailSender javaMailSender;
	
	public String passwordGenerator()
	{
		Random RANDOM = new SecureRandom();
	    String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	        int passwordLength = 8;
	        
	        StringBuilder returnValue = new StringBuilder(passwordLength);
	        
	        for (int i = 0; i < passwordLength; i++) {
	            returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
	        }
	        
	        return returnValue.toString();
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<?> getAgentById(@PathVariable("id") String id) {
		try {
			Agent agent = agentService.findById(id);

			return new ResponseEntity<>(agent, HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@GetMapping(value = "/user/{username}")
	public ResponseEntity<?> getAgentByBk(@PathVariable("username") String username) {
		try {
			Agent agent = agentService.findByBk(username);

			return new ResponseEntity<>(agent, HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	@PostMapping(value = "/")
	public ResponseEntity<?> insertAgent(@ModelAttribute AdminAgentInput adminAgentInput, @RequestParam(name = "pp", required = false) MultipartFile pp) {
		try {
			Agent ag = new Agent();
			User user = new User();
			
			String pass = passwordGenerator();
			String encryptedPassword = BCrypt.hashpw(pass, BCrypt.gensalt(12));

			ag.setEmail(adminAgentInput.getEmail());
			ag.setName(adminAgentInput.getName());
			ag.setStatus(Active.active);
			
			String msg = agentService.insert(ag);
			
			user.setUsername(adminAgentInput.getUsername());
			user.setPassword(encryptedPassword);
			user.setRole(Role.agent);
			user.setUser(agentService.findByBk(adminAgentInput.getEmail()).getId());

			userService.insert(user);
			
			Image img = new Image();
			
			if (pp != null) {
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
				
				Agent agn = agentService.findByBk(adminAgentInput.getUsername());
				
				imageService.insert(img);
				agn.setImageId(imageService.findByBk(fileName, data).getId());
				agentService.update(agn);
			}

			SimpleMailMessage email = new SimpleMailMessage();
	        //setTo(from, to)
	        email.setTo("jnat51.jg@gmail.com", adminAgentInput.getEmail());
	        
	        email.setSubject("Welcome "+ adminAgentInput.getName() +", New Agent!");
	        email.setText("Here is your username and password to login to your account.\nUsername: "+ adminAgentInput.getUsername()+ "\nPassword: " + pass);
	        
	        javaMailSender.send(email);
			
			return new ResponseEntity<>(msg, HttpStatus.CREATED);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@PatchMapping(value = "/image/{id}")
	public ResponseEntity<?> patchImage(@PathVariable String id, @RequestParam MultipartFile pp){
		try {
			Agent agent = agentService.findById(id);
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
			
			if(agent.getImageId() != null) {
				imageService.delete(agent.getImageId());
			}
			imageService.insert(img);
			
			agent.setImageId(imageService.findByBk(fileName, data).getId());
			agentService.update(agent);
			
			return new ResponseEntity<>("Profile picture updated", HttpStatus.OK);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@PatchMapping(value = "/status/{id}")
	public ResponseEntity<?> updateStatus(@PathVariable String id, @RequestBody Status status) {
		try {
			Agent agent = agentService.findById(id);
			
			agent.setStatus(status.getStatus());

			agentService.update(agent);
			return new ResponseEntity<>("Status changed to " + status.getStatus(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(value = "/")
	public ResponseEntity<?> updateAgent(@ModelAttribute Agent agent, @RequestParam(name= "pp", required = false) MultipartFile pp) {
		try {
			Agent ag = agentService.findById(agent.getId());
			
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
				
				if(agent.getImageId() != null) {
				imageService.delete(ag.getImageId());
				}
				imageService.insert(img);
				ag.setImageId(imageService.findByBk(fileName, data).getId());
			}
			
			agentService.update(ag);
			
			return new ResponseEntity<>("Update success", HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@GetMapping(value = "/")
	public ResponseEntity<?> getAllAgent(){
		try {
			List<Agent> agents = agentService.findAll();			
			
			return new ResponseEntity<>(agents, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping(value = "/status/{status}")
	public ResponseEntity<?> getAllWithStatus(@PathVariable String status){
		try {
			List<Agent> agents = agentService.findAllWithStatus(status);			
			
			return new ResponseEntity<>(agents, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> deleteAgent(@PathVariable String id) {
		try {
			Agent agent = agentService.findById(id);
			
			agentService.delete(id);
			
			if (agent.getImageId() != null) {
			imageService.delete(agent.getImageId());
			}
			
			return new ResponseEntity<>("Agent successfully deleted!", HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
}
