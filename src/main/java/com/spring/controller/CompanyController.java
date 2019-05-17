package com.spring.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import com.spring.model.Company;
import com.spring.model.Image;
import com.spring.service.CompanyService;
import com.spring.service.ImageService;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Controller
@RestController
@Transactional
@RequestMapping("/company")
public class CompanyController {
	@Autowired
	CompanyService companyService;
	@Autowired
	ImageService imageService;

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> deleteCompany(@PathVariable String id) {
		try {
			Company company = companyService.findCompanyById(id);
			
			companyService.deleteCompany(id);
			imageService.delete(company.getImageId());

			return new ResponseEntity<>("Company successfully deleted!", HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	@PostMapping(value = "/")
	public ResponseEntity<?> insertCompany(@RequestParam(name = "logo", required = false) MultipartFile logo,
			@ModelAttribute Company company) {
		try {
			if (logo != null) {
				Image img = new Image();
				byte[] data = logo.getBytes();
				
				Date date = new Date();
				SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
				String dateNow = dateFormat.format(date);
				String[] originalName = logo.getOriginalFilename().split("\\.");
				String fileName = originalName[0] + dateNow + "." + originalName[1];
				String mime = logo.getContentType();
				
				img.setImage(data);
				img.setFileName(fileName);
				img.setMime(mime);
				
				imageService.insert(img);
				company.setImageId(imageService.findByBk(fileName, data).getId());
			}

			String msg = companyService.insertCompany(company);

			return new ResponseEntity<>(msg, HttpStatus.CREATED);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	@PutMapping(value = "/")
	public ResponseEntity<?> updateCompany(@RequestParam(name = "logo", required = false) MultipartFile logo,
			@ModelAttribute Company company) {
		try {
			Company comp = companyService.findCompanyById(company.getId());
			Image image = new Image();

			byte[] data = logo.getBytes();
			String fileName = logo.getOriginalFilename();
			image.setImage(data);
			image.setFileName(fileName);
			image.setMime(logo.getContentType());

			if (logo != null) {
				imageService.delete(comp.getImageId());
				imageService.insert(image);
				company.setImageId(imageService.findByBk(fileName, data).getId());
			}

			String msg = companyService.insertCompany(company);

			return new ResponseEntity<>(msg, HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<?> getCompanyById(@PathVariable String id) {
		try {
			Company comp = companyService.findCompanyById(id);

			return new ResponseEntity<>(comp, HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	@GetMapping(value = "/code/{companyCode}")
	public ResponseEntity<?> getCompanyByBk(@PathVariable String companyCode) {
		try {
			Company comp = companyService.findCompanyByBk(companyCode);

			return new ResponseEntity<>(comp, HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
}
