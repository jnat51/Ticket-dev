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
import org.springframework.web.bind.annotation.DeleteMapping;
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
import com.spring.model.admin.AdminInput;
import com.spring.model.admin.AdminUpdate;
import com.spring.model.agent.Agent;
import com.spring.service.AdminUpdateService;
import com.spring.service.ImageService;
import com.spring.service.UserService;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Controller
@RestController
@RequestMapping("/newadmin")
public class AdminUpdateController {
	@Autowired
	AdminUpdateService adminUpdateService;
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
	public ResponseEntity<?> insertAdmin(@ModelAttribute AdminInput adminInput,
			@RequestParam(name = "pp", required = false) MultipartFile pp) {
		try {
			AdminUpdate adminUpdate = new AdminUpdate();
			User user = new User();

			adminUpdate.setActive(Active.active);
			adminUpdate.setEmail(adminInput.getEmail());
			adminUpdate.setName(adminInput.getName());

			adminUpdateService.insert(adminUpdate);

			String pass = passwordGenerator();
			String generatedSecuredPasswordHash = BCrypt.hashpw(pass, BCrypt.gensalt(12));

			user.setUsername(adminInput.getUsername());
			user.setPassword(generatedSecuredPasswordHash);
			user.setRole(Role.admin);
			user.setUser(adminUpdateService.findByBk(adminInput.getEmail()).getId());

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

				AdminUpdate admin = adminUpdateService.findByBk(adminUpdate.getEmail());

				imageService.insert(img);
				admin.setImageId(imageService.findByBk(fileName, data).getId());
				adminUpdateService.update(admin);
			}

			SimpleMailMessage email = new SimpleMailMessage();
			// setTo(from, to)
			email.setTo("jnat51.jg@gmail.com", adminInput.getEmail());

			email.setSubject("Welcome " + adminInput.getName() + ", New Admin!");
			email.setText("Here is your username and password to login to your account.\nUsername: "
					+ adminInput.getUsername() + "\nPassword: " + pass);

			System.out.println("send...");

			javaMailSender.send(email);

			System.out.println("sent");

			return new ResponseEntity<>("Admin successfuly inserted", HttpStatus.CREATED);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	@PutMapping(value = "/")
	public ResponseEntity<?> updateAdmin(@ModelAttribute AdminUpdate adminUpdate,
			@RequestParam(name = "pp", required = false) MultipartFile pp) {
		try {
			adminUpdateService.update(adminUpdate);

			return new ResponseEntity<>("Admin successfuly updated", HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@PatchMapping(value = "/{id}")
	public ResponseEntity<?> patchImage(@PathVariable String id, @RequestParam MultipartFile pp){
		try {
			AdminUpdate adminUpdate = adminUpdateService.findById(id);
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
			
			if (adminUpdate.getImageId() == null) {
			imageService.delete(adminUpdate.getImageId());
			}
			imageService.insert(img);
			
			adminUpdate.setImageId(imageService.findByBk(fileName, data).getId());
			adminUpdateService.update(adminUpdate);
			
			return new ResponseEntity<>("Profile picture updated", HttpStatus.OK);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@PatchMapping(value = "/status/{id}")
	public ResponseEntity<?> updateStatus(@PathVariable String id, @RequestBody Status status) {
		try {
			AdminUpdate adminUpdate = adminUpdateService.findById(id);
			
			adminUpdate.setActive(status.getStatus());

			adminUpdateService.update(adminUpdate);
			return new ResponseEntity<>("Status changed to " + status.getStatus(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
}
