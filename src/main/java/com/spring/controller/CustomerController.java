package com.spring.controller;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.spring.model.Company;
import com.spring.model.Image;
import com.spring.model.Status;
import com.spring.model.UpdatePassword;
import com.spring.model.customer.Customer;
import com.spring.model.customer.CustomerLogin;
import com.spring.model.customer.CustomerWithImage;
import com.spring.service.CompanyService;
import com.spring.service.CustomerService;
import com.spring.service.ImageService;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Controller
@RestController
@RequestMapping("/customer")
public class CustomerController {
	@Autowired
	CompanyService companyService;
	@Autowired
	CustomerService customerService;
	@Autowired
	ImageService imageService;
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

	@PostMapping(value = "/")
	public ResponseEntity<?> insertCustomer(@RequestParam(name = "pp", required = false) MultipartFile image,
			@ModelAttribute Customer customer) {
		try {
			Customer cust = customerService.findCustomerById(customer.getId());
			String pass = passwordGenerator();
			String generatedSecuredPasswordHash = BCrypt.hashpw(pass, BCrypt.gensalt(12));

			cust.setEmail(customer.getEmail());
			cust.setUsername(customer.getUsername());
			cust.setPassword(generatedSecuredPasswordHash);
			cust.setName(customer.getName());
			cust.setPosition(customer.getPosition());
			cust.setCompany(companyService.findCompanyById(customer.getCompany().getId()));
			cust.setStatus(Active.active);
			System.out.println(customer.getCompany().getId());
			
			if (image != null) {
				Image img = new Image();
				byte[] data = image.getBytes();

				Date date = new Date();
				SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
				String dateNow = dateFormat.format(date);
				String[] originalName = image.getOriginalFilename().split("\\.");
				String fileName = originalName[0] + dateNow + "." + originalName[1];
				String mime = image.getContentType();

				img.setImage(data);
				img.setFileName(fileName);
				img.setMime(mime);

				imageService.insert(img);
				System.out.println(imageService.findByBk(fileName, data).getId());
				cust.setImageId(imageService.findByBk(fileName, data).getId());
			}

			customerService.insertCustomer(cust);
			try {
			SimpleMailMessage email = new SimpleMailMessage();
			// setTo(from, to)
			email.setTo("jnat51.jg@gmail.com", customer.getEmail());

			email.setSubject("Welcome to Linov Support, " + customer.getName() + "!");
			email.setText("Here is your username and password to login to your account.\nUsername: "
					+ customer.getUsername() + "\nPassword: " + pass);

			System.out.println("send...");

			javaMailSender.send(email);

			System.out.println("sent");

			return new ResponseEntity<>("Customer successfuly updated", HttpStatus.CREATED);
			} catch (Exception e) {
				return new ResponseEntity<>("Failed to send email", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(value = "/")
	public ResponseEntity<?> updateCustomer(@RequestParam(name = "pp", required = false) MultipartFile image,
			@ModelAttribute Customer customer) {
		try {
			Customer cust = customerService.findCustomerById(customer.getId());
			String pass = customer.getPassword();
			String generatedSecuredPasswordHash = BCrypt.hashpw(pass, BCrypt.gensalt(12));

			cust.setEmail(customer.getEmail());
			cust.setUsername(customer.getUsername());
			cust.setPassword(generatedSecuredPasswordHash);
			cust.setName(customer.getName());

			if (image != null) {
				Image img = new Image();
				byte[] data = image.getBytes();

				Date date = new Date();
				SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
				String dateNow = dateFormat.format(date);
				String[] originalName = image.getOriginalFilename().split("\\.");
				String fileName = originalName[0] + dateNow + "." + originalName[1];
				String mime = image.getContentType();

				img.setImage(data);
				img.setFileName(fileName);
				img.setMime(mime);

				if(cust.getImageId() != null) {
				imageService.delete(cust.getImageId());
				}
				imageService.insert(img);
				cust.setImageId(imageService.findByBk(fileName, data).getId());
			}

			customerService.updateCustomer(cust);

			return new ResponseEntity<>("Customer successfuly update", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PatchMapping(value = "/image/{id}")
	public ResponseEntity<?> patchImage(@PathVariable String id, @RequestParam MultipartFile pp){
		try {
			Customer customer = customerService.findCustomerById(id);
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
			
			if(customer.getImageId() != null) {
				imageService.delete(customer.getImageId());
				}
			imageService.insert(img);
			
			customer.setImageId(imageService.findByBk(fileName, data).getId());
			customerService.updateCustomer(customer);
			
			return new ResponseEntity<>("Profile picture updated", HttpStatus.OK);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@PatchMapping(value = "/status/{id}")
	public ResponseEntity<?> updateStatus(@PathVariable String id, @RequestBody Status status) {
		try {
			Customer customer = customerService.findCustomerById(id);
			
			customer.setStatus(status.getStatus());

			customerService.updateCustomer(customer);
			return new ResponseEntity<>("Status changed to " + status.getStatus(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> deleteCustomer(@PathVariable String id) {
		try {
			Customer customer = customerService.findCustomerById(id);

			customerService.deleteCustomer(id);

			if (customer.getImageId() != null) {
				imageService.delete(customer.getImageId());
			}

			return new ResponseEntity<>("Company successfully deleted!", HttpStatus.OK);
		} catch (

		Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<?> getCustomerById(@PathVariable String id) {
		try {
			Customer cust = customerService.findCustomerById(id);

			List<Customer> customers = new ArrayList<Customer>();

			Company company = cust.getCompany();
			company.setCustomers(customers);
			
			cust.setCompany(company);

			return new ResponseEntity<>(cust, HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@GetMapping(value = "/test/{id}")
	public ResponseEntity<?> test(@PathVariable String id) {
		try {
			CustomerWithImage cust = customerService.findCustomerWithImage(id);

			return new ResponseEntity<>(cust, HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	@GetMapping(value = "/")
	public ResponseEntity<?> getAllCustomer() {
		try {
			System.out.println("find all");

			List<Customer> customers = customerService.findAll();

			System.out.println(customers.size());

			for (Customer cust : customers) {
				Company comp = cust.getCompany();
				comp.setCustomers(new ArrayList<Customer>());

				cust.setCompany(comp);
			}

			return new ResponseEntity<>(customers, HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	@GetMapping(value = "/user/{username}")
	public ResponseEntity<?> getCustomerByBk(@PathVariable String username) {
		try {
			Customer cust = customerService.findCustomerByBk(username);

			List<Customer> customers = new ArrayList<Customer>();

			Company company = cust.getCompany();
			company.setCustomers(customers);

			return new ResponseEntity<>(cust, HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	@GetMapping(value = "/login")
	public ResponseEntity<?> login(@RequestBody Customer customer) {
		try {
			boolean matched = BCrypt.checkpw(customer.getPassword(),
					customerService.findCustomerByBk(customer.getUsername()).getPassword());
			System.out.println(matched);

			CustomerLogin cust;
			
			if(matched == true) {
				cust = customerService.login(customer.getUsername());
				
				return new ResponseEntity<>(cust, HttpStatus.OK);
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wrong username/password");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	@PatchMapping(value = "/password/{id}")
	public ResponseEntity<?> updatePassword(@RequestBody UpdatePassword updatePassword, @PathVariable String id) {
		try {
			Customer customer = customerService.findCustomerById(id);

			if (BCrypt.checkpw(updatePassword.getOldPassword(), customer.getPassword()) == true) {

				String pass = updatePassword.getNewPassword();
				String generatedSecuredPasswordHash = BCrypt.hashpw(pass, BCrypt.gensalt(12));

				customer.setPassword(generatedSecuredPasswordHash);

				customerService.updateCustomer(customer);

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
			Customer customer = customerService.resetPassword(email);

			String pass = passwordGenerator();
			String generatedSecuredPasswordHash = BCrypt.hashpw(pass, BCrypt.gensalt(12));

			customer.setPassword(generatedSecuredPasswordHash);

			customerService.updateCustomer(customer);

			try {
				SimpleMailMessage mail = new SimpleMailMessage();
				// setTo(from, to)
				mail.setTo("jnat51.jg@gmail.com", email);

				mail.setSubject("Hi " + customer.getName());
				mail.setText("Here is your new password to login to your account. \nPassword: " + pass);

				System.out.println("send...");

				javaMailSender.send(mail);

				System.out.println("sent");
			} catch (Exception e) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to send email");
			}

			return new ResponseEntity<>("Password has been reset.", HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@GetMapping(value = "/status/{status}")
	public ResponseEntity<?> getAllCustomerWithStatus(@PathVariable String status) {
		try {
			List<Customer> customers = customerService.findWithStatus(status);

			System.out.println(customers.size());

			for (Customer cust : customers) {
				Company comp = cust.getCompany();
				comp.setCustomers(new ArrayList<Customer>());

				cust.setCompany(comp);
			}

			return new ResponseEntity<>(customers, HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
}
