package com.spring.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.dao.AdminUpdateDao;
import com.spring.exception.ErrorException;
import com.spring.model.admin.AdminLogin;
import com.spring.model.admin.AdminUpdate;

@Service
public class AdminUpdateService {
	@Autowired
	AdminUpdateDao adminUpdateDao;
	
	public void insert(AdminUpdate adminUpdate) throws ErrorException {
		if(adminUpdateDao.isIdExist(adminUpdate.getId()) == true)
		{
			throw new ErrorException("Admin already exist.");
		}
		if(adminUpdateDao.isBkExist(adminUpdate.getEmail()) == true)
		{
			throw new ErrorException("Username already exist!");
		}
		
		adminUpdateDao.save(adminUpdate);
	}
	
	public void delete(String id) throws ErrorException
	{
		if(adminUpdateDao.isIdExist(id) == true)
		{
			adminUpdateDao.delete(adminUpdateDao.findById(id));
		}
		else
		{
			throw new ErrorException("Admin not found!");
		}
	}
	
	public void update(AdminUpdate adminUpdate) throws ErrorException
	{
		if(adminUpdateDao.isIdExist(adminUpdate.getId()) == false)
		{
			throw new ErrorException("Admin not found!");
		}
		if(adminUpdateDao.isBkExist(adminUpdate.getEmail()) == false)
		{
			throw new ErrorException("Admin not found!");
		}
		if(!adminUpdate.getEmail().equals(adminUpdateDao.findById(adminUpdate.getId()).getEmail()))
		{
			throw new ErrorException("Username cannot be changed!");
		}
		adminUpdateDao.save(adminUpdate);
	}
	
	public AdminUpdate findById(String id)
	{
		AdminUpdate adminUpdate = new AdminUpdate();
		
		if(adminUpdateDao.findById(id) != null)
		{
			return adminUpdateDao.findById(id);
		}
		else
		{
			return adminUpdate;
		}
	}
	
	public AdminUpdate findByBk(String email)
	{
		AdminUpdate adminUpdate = new AdminUpdate();
		
		if(adminUpdateDao.findByBk(email) != null)
		{
			return adminUpdateDao.findByBk(email);
		}
		else
		{
			return adminUpdate;
		}
	}
	
	public List<AdminUpdate> findAll()
	{
		List<AdminUpdate> adminUpdate = new ArrayList<AdminUpdate>();
		
		if(adminUpdateDao.findAll().size() > 0)
		{
			return adminUpdateDao.findAll();
		}
		else
		{
			return adminUpdate;
		}
	}
	
	public AdminLogin login(String username)
	{
		AdminLogin admin = new AdminLogin();
		
		if(adminUpdateDao.login(username) != null)
		{
			admin = adminUpdateDao.login(username);
			
			return admin;
		}
		else
		{
			return admin;
		}
	}
}
