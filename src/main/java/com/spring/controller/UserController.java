package com.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.model.UpdatePassword;
import com.spring.model.User;
import com.spring.service.UserService;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Controller
@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	UserService userService;
	
	@PutMapping(value = "/")
	public ResponseEntity<?> updateUser(@ModelAttribute User user){
		try {
			String pass = user.getPassword();
			String generatedSecuredPasswordHash = BCrypt.hashpw(pass, BCrypt.gensalt(12));
			
			user.setPassword(generatedSecuredPasswordHash);
			userService.update(user);
					
			return new ResponseEntity<>("Admin successfuly inserted", HttpStatus.OK);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@PatchMapping(value="/{userId}")
	public ResponseEntity<?> patchPassword(@RequestBody UpdatePassword updatePassword, @PathVariable String userId){
		try {
			User user = userService.findByUserId(userId);
			
			if (BCrypt.checkpw(updatePassword.getOldPassword(), user.getPassword()) == true) {
				String pass = updatePassword.getNewPassword();
				String generatedSecuredPasswordHash = BCrypt.hashpw(pass, BCrypt.gensalt(12));
				
				user.setPassword(generatedSecuredPasswordHash);
				
				userService.update(user);
				return new ResponseEntity<>("Update password success", HttpStatus.OK);
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password not match");
			}
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
}
