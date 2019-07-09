package com.spring.controller;

import java.security.SecureRandom;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.enumeration.Enum.Active;
import com.spring.enumeration.Enum.Role;
import com.spring.model.UpdatePassword;
import com.spring.model.User;
import com.spring.model.admin.Admin;
import com.spring.model.admin.AdminLogin;
import com.spring.model.agent.Agent;
import com.spring.model.agent.AgentLogin;
import com.spring.model.customer.Customer;
import com.spring.model.customer.CustomerLogin;
import com.spring.service.AdminService;
import com.spring.service.AgentService;
import com.spring.service.CustomerService;
import com.spring.service.UserService;

import ch.qos.logback.core.status.Status;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Controller
@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	UserService userService;
	@Autowired
	AgentService agentService;
	@Autowired
	AdminService adminService;
	@Autowired
	CustomerService customerService;
	@Autowired
	private JavaMailSender javaMailSender;

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

	@PutMapping(value = "/")
	public ResponseEntity<?> updateUser(@ModelAttribute User user) {
		try {
			String pass = user.getPassword();
			String generatedSecuredPasswordHash = BCrypt.hashpw(pass, BCrypt.gensalt(12));

			user.setPassword(generatedSecuredPasswordHash);
			userService.update(user);

			return new ResponseEntity<>("Admin successfuly inserted", HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	@PatchMapping(value = "/{userId}")
	public ResponseEntity<?> patchPassword(@RequestBody UpdatePassword updatePassword, @PathVariable String userId) {
		try {
			User user = userService.findByUserId(userId);
			System.out.println(user.getUsername());
			System.out.println(user.getPassword());

			if (BCrypt.checkpw(updatePassword.getOldPassword(), user.getPassword()) == true) {
				String pass = updatePassword.getNewPassword();
				String generatedSecuredPasswordHash = BCrypt.hashpw(pass, BCrypt.gensalt(12));

				user.setPassword(generatedSecuredPasswordHash);

				userService.update(user);
				return new ResponseEntity<>("Update password success", HttpStatus.OK);
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password not match");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	@GetMapping(value = "/login/{username}/{password}")
	public ResponseEntity<?> login(@PathVariable String username, @PathVariable String password) {
		try {
			boolean matched = BCrypt.checkpw(password, userService.findByBk(username).getPassword());
			System.out.println(matched);

			User login = userService.findByBk(username);
			Object user = new Object();

			System.out.println(login.getRole());

			if (matched == true) {
				if (login.getRole() == Role.admin) {
					user = (AdminLogin) userService.login(username, login.getRole());
					AdminLogin admin = (AdminLogin) userService.login(username, login.getRole());
				}
				if (login.getRole() == Role.agent) {
					AgentLogin agent = (AgentLogin) userService.login(username, login.getRole());
					if (agent.getStatus().equals("active")) {
						user = (AgentLogin) userService.login(username, login.getRole());
					}else {
						return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already unactive");
					}
				}
				if (login.getRole() == Role.customer) {
					CustomerLogin customer = (CustomerLogin) userService.login(username, login.getRole());
					if (customer.getStatus().equals("active")) {
						user = (CustomerLogin) userService.login(username, login.getRole());
					}else {
						return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already unactive");
					}
				}
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wrong username/password");
			}

			return new ResponseEntity<>(user, HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	@PatchMapping(value = "/reset")
	public ResponseEntity<?> resetPassword(@RequestParam String username) {
		try {
			User user = userService.findByBk(username);
			String name;
			String email;
			SimpleMailMessage mail = new SimpleMailMessage();

			String pass = passwordGenerator();
			String generatedSecuredPasswordHash = BCrypt.hashpw(pass, BCrypt.gensalt(12));
			if (user.getRole() == Role.admin) {
				try {
					System.out.println("admin");
					Admin admin = adminService.findById(user.getUser());
					name = admin.getName();
					email = admin.getEmail();

					user.setPassword(generatedSecuredPasswordHash);

					userService.update(user);

					// setTo(from, to)
					mail.setTo("jnat51.jg@gmail.com", email);

					mail.setSubject("Hi " + name);
					mail.setText("Here is your new password to login to your account. \nPassword: " + pass);

					System.out.println("send...");

					javaMailSender.send(mail);

					System.out.println("sent");
				} catch (Exception e) {
					System.out.println(e);
				}
			} else if (user.getRole() == Role.agent) {
				System.out.println("agent");
				Agent agent = agentService.findById(user.getUser());
				name = agent.getName();
				email = agent.getEmail();

				user.setPassword(generatedSecuredPasswordHash);

				userService.update(user);

				// setTo(from, to)
				mail.setTo("jnat51.jg@gmail.com", email);

				mail.setSubject("Hi " + name);
				mail.setText("Here is your new password to login to your account. \nPassword: " + pass);

				System.out.println("send...");

				javaMailSender.send(mail);

				System.out.println("sent");
			} else if (user.getRole() == Role.customer) {
				System.out.println("customer");
				Customer customer = customerService.findCustomerById(user.getUser());
				name = customer.getName();
				email = customer.getEmail();

				user.setPassword(generatedSecuredPasswordHash);

				userService.update(user);

				// setTo(from, to)
				mail.setTo("jnat51.jg@gmail.com", email);

				mail.setSubject("Hi " + name);
				mail.setText("Here is your new password to login to your account. \nPassword: " + pass);

				System.out.println("send...");

				javaMailSender.send(mail);

				System.out.println("sent");
			}

			return new ResponseEntity<>("Password has been reset.", HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
}
