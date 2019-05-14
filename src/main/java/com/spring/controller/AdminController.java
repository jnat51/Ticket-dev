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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.spring.model.Admin;
import com.spring.model.Image;
import com.spring.service.AdminService;
import com.spring.service.ImageService;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Controller
@RestController
@Transactional
@RequestMapping("/admin")
public class AdminController {
	@Autowired
	AdminService adminService;
	@Autowired
	ImageService imageService;

	@GetMapping(value = "/{id}")
	public ResponseEntity<?> getAdminById(@PathVariable("id") String id) {
		try {
			Admin admin = adminService.findById(id);

			return new ResponseEntity<>(admin, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage() , HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping(value = "/user/{username}")
	public ResponseEntity<?> getAdminByBk(@PathVariable("username") String username) {
		try {
			Admin admin = adminService.findByBk(username);

			return new ResponseEntity<>(admin, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage() , HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping(value = "/")
	public ResponseEntity<?> insertAdmin(@ModelAttribute Admin admin, @RequestParam(required = false) MultipartFile pp) {
		try {
			Admin adm = new Admin();
			String pass = admin.getPassword();
			String generatedSecuredPasswordHash = BCrypt.hashpw(pass, BCrypt.gensalt(12));
			
			Image img = new Image();
			byte[] data = pp.getBytes();
			String fileName = pp.getOriginalFilename();
			String mime = pp.getContentType();
			
			img.setImage(data);
			img.setFileName(fileName);
			img.setMime(mime);
			
			adm.setEmail(admin.getEmail());
			adm.setUsername(admin.getUsername());
			adm.setPassword(generatedSecuredPasswordHash);
			adm.setName(admin.getName());
			
			System.out.println(imageService.findByBk(fileName, data).getId());
			
			if (pp.toString().isEmpty() == false) {
				imageService.insert(img);
				adm.setImageId(imageService.findByBk(fileName, data).getId());
			}
			
			String msg = adminService.insert(adm);
			
			return new ResponseEntity<>(msg , HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage() , HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping(value = "/")
	public ResponseEntity<?> updateAdmin(@ModelAttribute Admin admin, @RequestParam(name = "pp",required = false) MultipartFile pp) {
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

			if (pp.toString().isEmpty() == false) {
				Image img = new Image();
				byte[] data = pp.getBytes();
				String fileName = pp.getOriginalFilename();
				String mime = pp.getContentType();
				
				img.setImage(data);
				img.setFileName(fileName);
				img.setMime(mime);
				
				System.out.println(adm.getImageId());
				imageService.delete(adm.getImageId());
				imageService.insert(img);
				adm.setImageId(imageService.findByBk(fileName, data).getId());
			}
			
			adminService.update(adm);
			
			return new ResponseEntity<>("Update success", HttpStatus.CREATED);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> deleteAdmin(@PathVariable String id) {
		try {
			adminService.delete(id);
			imageService.delete(adminService.findById(id).getImageId());

			return new ResponseEntity<>("Admin successfully deleted!", HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
}
