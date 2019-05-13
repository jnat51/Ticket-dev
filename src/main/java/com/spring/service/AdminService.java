package com.spring.service;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.dao.AdminDao;
import com.spring.model.Admin;

@Service
public class AdminService {
	@Autowired
	AdminDao adminDao;
	
	public void delete(String id) throws ServiceException
	{
		if(adminDao.isIdExist(id) == true)
		{
			adminDao.delete(adminDao.findById(id));
		}
		else
		{
			throw new ServiceException("Admin not found!");
		}
	}
	
	public String insert(Admin admin) throws ServiceException
	{
		if(adminDao.isIdExist(admin.getId()) == true)
		{
			throw new ServiceException("Admin already exist.");
		}
		if(adminDao.isBkExist(admin.getUsername()) == true)
		{
			throw new ServiceException("Username already exist!");
		}
			adminDao.save(admin);
			return "New Admin account successfully created";
	}
	
	public void update(Admin admin) throws ServiceException
	{
		if(adminDao.isIdExist(admin.getId()) == false)
		{
			throw new ServiceException("Admin not found!");
		}
		if(adminDao.isBkExist(admin.getUsername()) == false)
		{
			throw new ServiceException("Admin not found!");
		}
		if(!admin.getUsername().equals(adminDao.findById(admin.getId()).getUsername()))
		{
			throw new ServiceException("Username cannot be changed!");
		}
		adminDao.save(admin);
	}
	
	public Admin findByBk(String username)
	{
		Admin admin = new Admin();
		
		if(adminDao.findByBk(username) != null)
		{
			admin = adminDao.findByBk(username);
			
			return admin;
		}
		else
		{
			return admin;
		}
	}
	
	public Admin findById(String id)
	{
		Admin admin = new Admin();
		
		if(adminDao.findById(id) != null)
		{
			return adminDao.findById(id);
		}
		else
		{
			return admin;
		}
	}
}
