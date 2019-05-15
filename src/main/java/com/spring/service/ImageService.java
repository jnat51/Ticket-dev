package com.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.dao.ImageDao;
import com.spring.exception.ErrorException;
import com.spring.model.Image;

@Service
public class ImageService {
	@Autowired
	ImageDao imageDao;

	public String insert(Image image) throws ErrorException {
		if (imageDao.isIdExist(image.getId()) == true) {
			throw new ErrorException("Image already exist.");
		}
		imageDao.saveImage(image);
		return "New image successfully added";
	}

	public void delete(String id) throws ErrorException {
		if (imageDao.isIdExist(id) == true) {
			imageDao.deleteImage(imageDao.findById(id));
		} else {
			throw new ErrorException("Image not found!");
		}
	}

	public void update(Image image) throws ErrorException {
		if (imageDao.isIdExist(image.getId()) == false) {
			throw new ErrorException("Image not found!");
		}
		imageDao.saveImage(image);
	}
	
	public Image findById(String id)
	{
		Image image = new Image();
		
		if(imageDao.findById(id) != null)
		{
			return imageDao.findById(id);
		}
		else
		{
			return image;
		}
	}
	
	public Image findByBk(String fileName, byte[] data)
	{
		Image image = new Image();
		
		if(imageDao.findByBk(fileName, data) != null)
		{
			return imageDao.findByBk(fileName, data);
		}
		else
		{
			return image;
		}
	}
}
