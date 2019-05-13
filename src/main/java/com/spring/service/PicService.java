package com.spring.service;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.dao.PicDao;
import com.spring.model.Pic;

@Service
public class PicService {
	@Autowired
	PicDao picDao;
	
	public void insert(Pic pic) {
		if(picDao.isIdExist(pic.getId()) == true)
		{
			throw new ServiceException("Pic already exist.");
		}
		if(picDao.isBkExist(pic.getCompany().getId()) == true)
		{
			throw new ServiceException("Pic already exist!");
		}
		picDao.save(pic);
	}
	
	public void delete(String id) throws ServiceException
	{
		if(picDao.isIdExist(id) == true)
		{
			picDao.delete(picDao.findById(id));
		}
		else
		{
			throw new ServiceException("Pic not found!");
		}
	}
	
	public void update(Pic pic) throws ServiceException
	{
		if(picDao.isIdExist(pic.getId()) == false)
		{
			throw new ServiceException("Pic not found!");
		}
		if(picDao.isBkExist(pic.getCompany().getId()) == false)
		{
			throw new ServiceException("Pic not found!");
		}
		if(!pic.getCompany().getId().equals(picDao.findById(pic.getId()).getCompany().getId()))
		{
			throw new ServiceException("Username cannot be changed!");
		}
		picDao.save(pic);
	}
	
	public Pic findById(String id)
	{
		Pic pic = new Pic();
		
		if(picDao.findById(id) != null)
		{
			return picDao.findById(id);
		}
		else
		{
			return pic;
		}
	}
	
	public Pic findByBk(String companyId)
	{
		Pic pic = new Pic();
		
		if(picDao.findByBk(companyId) != null)
		{
			return picDao.findByBk(companyId);
		}
		else
		{
			return pic;
		}
	}
}
