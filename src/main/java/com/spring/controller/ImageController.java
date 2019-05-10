package com.spring.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.spring.model.Image;
import com.spring.service.ImageService;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Controller
@RestController
@Transactional
@RequestMapping("/image")
public class ImageController {
	@Autowired
	ImageService imageService;
	
	@PostMapping(value = "/")
	public ResponseEntity<?> insertAgent(@RequestParam("image") MultipartFile file) {
		try {
			Image img = new Image();
			String fileName = file.getOriginalFilename();
			
			byte[] data = file.getBytes();
			
			img.setImage(data);
			img.setFileName(fileName);
			return new ResponseEntity<>(imageService.insert(img), HttpStatus.CREATED);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@DeleteMapping(value="/{id}")
	public ResponseEntity<?> deleteImage(@PathVariable String id) {
		try {
		imageService.delete(id);
		
		return new ResponseEntity<>("Image successfully deleted", HttpStatus.OK);
		}
		catch(Exception e)
		{
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@PutMapping(value="/")
	public ResponseEntity<?> updateImage(@RequestParam("image") MultipartFile file, @RequestParam("id") String id) {
		try {
			Image img = new Image();
			String fileName = file.getOriginalFilename();
			
			byte[] data = file.getBytes();
			
			img.setId(id);
			img.setImage(data);
			img.setFileName(fileName);
			imageService.update(img);
			
			return new ResponseEntity<>("Image successfully updated" ,HttpStatus.CREATED);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> getImage(@PathVariable String id)
	{
		try {
			Image img = imageService.findById(id);
			
			ByteArrayInputStream bis = new ByteArrayInputStream(img.getImage());
		    BufferedImage bImage2 = ImageIO.read(bis);
			ImageIO.write(bImage2, "jpg", new File("C:/Users/Student2014/eclipse-workspace/email/src/main/java/com/mkyong/"+ img.getFileName()));

			return new ResponseEntity<>(img, HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
}
