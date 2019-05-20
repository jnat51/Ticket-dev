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
import org.springframework.transaction.annotation.Transactional;
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

import com.spring.model.Admin;
import com.spring.model.Image;
import com.spring.model.UpdatePassword;
import com.spring.service.AdminService;
import com.spring.service.ImageService;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Controller
@RestController
@RequestMapping("/admin")
public class AdminController {
	@Autowired
	AdminService adminService;
	@Autowired
	ImageService imageService;
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

	@GetMapping(value = "/{id}")
	public ResponseEntity<?> getAdminById(@PathVariable("id") String id) {
		try {
			Admin admin = adminService.findById(id);

			return new ResponseEntity<>(admin, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(value = "/user/{username}")
	public ResponseEntity<?> getAdminByBk(@PathVariable("username") String username) {
		try {
			Admin admin = adminService.findByBk(username);

			return new ResponseEntity<>(admin, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping(value = "/")
	public ResponseEntity<?> insertAdmin(@ModelAttribute Admin admin,
			@RequestParam(name = "pp", required = false) MultipartFile pp) {
		try {
			Admin adm = new Admin();

			String pass = passwordGenerator();
			String generatedSecuredPasswordHash = BCrypt.hashpw(pass, BCrypt.gensalt(12));

			adm.setEmail(admin.getEmail());
			adm.setUsername(admin.getUsername());
			adm.setPassword(generatedSecuredPasswordHash);
			adm.setName(admin.getName());

			String msg = adminService.insert(adm);

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

				Admin ad = adminService.findByBk(adm.getUsername());

				imageService.insert(img);
				ad.setImageId(imageService.findByBk(fileName, data).getId());
				adminService.update(ad);
			}

			SimpleMailMessage email = new SimpleMailMessage();
			// setTo(from, to)
			email.setTo("jnat51.jg@gmail.com", admin.getEmail());

			email.setSubject("Welcome " + admin.getName() + ", New Admin!");
			email.setText("Here is your username and password to login to your account.\nUsername: "
					+ admin.getUsername() + "\nPassword: " + pass);

			System.out.println("send...");

			javaMailSender.send(email);

			System.out.println("sent");

			return new ResponseEntity<>(msg, HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	@PutMapping(value = "/")
	public ResponseEntity<?> updateAdmin(@ModelAttribute Admin admin,
			@RequestParam(name = "pp", required = false) MultipartFile pp) {
		try {
			Admin adm = adminService.findById(admin.getId());
			String pass = admin.getPassword();
			String generatedSecuredPasswordHash = BCrypt.hashpw(pass, BCrypt.gensalt(12));

			System.out.println(pass);
			System.out.println(generatedSecuredPasswordHash);

			adm.setId(admin.getId());
			adm.setEmail(admin.getEmail());
			adm.setUsername(admin.getUsername());
			adm.setPassword(generatedSecuredPasswordHash);
			adm.setName(admin.getName());

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

				if (adm.getImageId() == null) {
					imageService.insert(img);
					adm.setImageId(imageService.findByBk(fileName, data).getId());
				} else {
					System.out.println(adm.getImageId());
					imageService.delete(adm.getImageId());
					imageService.insert(img);
					adm.setImageId(imageService.findByBk(fileName, data).getId());
				}
			}

			adminService.update(adm);

			return new ResponseEntity<>("Update success", HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> deleteAdmin(@PathVariable String id) {
		try {
			Admin admin = adminService.findById(id);

			adminService.delete(id);
			
			if (admin.getImageId() != null) {
				imageService.delete(admin.getImageId());
			}

			return new ResponseEntity<>("Admin successfully deleted!", HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

//	@GetMapping(value = "/login")
//	public ResponseEntity<?> login(@RequestBody Admin admin) {
//		try {
//			System.out.println(adminService.findByBk(admin.getUsername()).getPassword());
//			boolean matched = BCrypt.checkpw(admin.getPassword(),
//					adminService.findByBk(admin.getUsername()).getPassword());
//			System.out.println(matched);
//
//			return new ResponseEntity<>(matched, HttpStatus.OK);
//		} catch (Exception e) {
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//		}
//	}

	@GetMapping(value = "/login")
	public ResponseEntity<?> login(@RequestBody Admin admin) {
		try {
			adminService.login(admin.getUsername(), admin.getPassword());
			boolean matched = BCrypt.checkpw(admin.getPassword(),
					adminService.findByBk(admin.getUsername()).getPassword());
			System.out.println(matched);
			
			String msg;
			
			if(matched == true) {
				msg = "Login success!";
			} else {
				msg = "Wrong username/password";
			}

			return new ResponseEntity<>(msg, HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	@PatchMapping(value = "/password/{id}")
	public ResponseEntity<?> updatePassword(@RequestBody UpdatePassword updatePassword, @PathVariable String id) {
		try {
			Admin adm = adminService.findById(id);
			if (BCrypt.checkpw(updatePassword.getOldPassword(), adm.getPassword()) == true) {

				String pass = updatePassword.getNewPassword();
				String generatedSecuredPasswordHash = BCrypt.hashpw(pass, BCrypt.gensalt(12));

				adm.setPassword(generatedSecuredPasswordHash);

				adminService.update(adm);
				
				return new ResponseEntity<>("Update password success", HttpStatus.OK);
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password not match");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@PatchMapping(value = "/reset")
	public ResponseEntity<?> resetPassword(@RequestParam String email) {
		try {
			Admin admin = adminService.findByEmail(email);
			
			System.out.println(admin.getId());
			
			String pass = passwordGenerator();
			String generatedSecuredPasswordHash = BCrypt.hashpw(pass, BCrypt.gensalt(12));
			
			admin.setPassword(generatedSecuredPasswordHash);
			
			adminService.update(admin);
			
			SimpleMailMessage mail = new SimpleMailMessage();
			// setTo(from, to)
			mail.setTo("jnat51.jg@gmail.com", email);

			mail.setSubject("Hi " + admin.getName());
			mail.setText("Here is your new password to login to your account. \nPassword: " + pass);

			System.out.println("send...");

			javaMailSender.send(mail);

			System.out.println("sent");
			
			return new ResponseEntity<>("Password has been reset.", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
}
