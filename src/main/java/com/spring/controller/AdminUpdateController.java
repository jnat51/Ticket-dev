package com.spring.controller;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.spring.enumeration.Enum.Active;
import com.spring.enumeration.Enum.Role;
import com.spring.model.Image;
import com.spring.model.User;
import com.spring.model.admin.AdminInput;
import com.spring.model.admin.AdminUpdate;
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
	public ResponseEntity<?> insertRole(@ModelAttribute AdminInput adminInput,
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
			
			return new ResponseEntity<>("Admin successfuly inserted", HttpStatus.CREATED);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
}
