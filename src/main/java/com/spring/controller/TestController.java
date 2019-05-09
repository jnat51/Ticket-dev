package com.spring.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.spring.dao.TestDao;
import com.spring.model.Test;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Controller
@RestController
@Transactional
@RequestMapping("/test")
public class TestController {
	@Autowired
	TestDao testDao;

	@PostMapping(value = "/")
	public ResponseEntity<?> insertAgent(@RequestParam("image") MultipartFile file) throws IOException {
		try {
			Test tes = new Test();

			byte[] a = file.getBytes();
			tes.setImage(a);

			testDao.insert(tes);

			return new ResponseEntity<>("success", HttpStatus.CREATED);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
}
