package com.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.dao.AdminDao;
import com.spring.exception.ErrorException;
import com.spring.model.Admin;

@Service
public class AdminService {
	@Autowired
	AdminDao adminDao;
	
	public void delete(String id) throws ErrorException
	{
		if(adminDao.isIdExist(id) == true)
		{
			adminDao.delete(adminDao.findById(id));
		}
		else
		{
			throw new ErrorException("Admin not found!");
		}
	}
	
	public String insert(Admin admin) throws ErrorException
	{
		if(adminDao.isIdExist(admin.getId()) == true)
		{
			throw new ErrorException("Admin already exist.");
		}
		if(adminDao.isBkExist(admin.getUsername()) == true)
		{
			throw new ErrorException("Username already exist!");
		}
		if(adminDao.isEmailExist(admin.getEmail()) == true) {
			throw new ErrorException("Email already exist!");
		}
		
			adminDao.save(admin);
			return "New Admin account successfully created";
	}
	
	public void update(Admin admin) throws ErrorException
	{
		if(adminDao.isIdExist(admin.getId()) == false)
		{
			throw new ErrorException("Admin not found!");
		}
		if(adminDao.isBkExist(admin.getUsername()) == false)
		{
			throw new ErrorException("Admin not found!");
		}
		if(adminDao.isEmailExist(admin.getEmail()) == false) {
			throw new ErrorException("Email not registered!");
		}
		if(!admin.getUsername().equals(adminDao.findById(admin.getId()).getUsername()))
		{
			throw new ErrorException("Username cannot be changed!");
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
	
	public Admin login(String username, String password) throws ErrorException
	{
		Admin admin = new Admin();
		
		if(adminDao.login(username, password) == null) {
			throw new ErrorException("Wrong username/password!");
		}
		else {
			return admin;
		}
	}
	
	public boolean passwordVerification(String password) throws ErrorException
	{
		if(adminDao.passwordVerification(password) == false) {
			throw new ErrorException("Password is not match.");
		}
		else {
			return true;
		}
	}
	
	public Admin findByEmail(String email) throws ErrorException
	{
		Admin admin = new Admin();
		
		if(adminDao.findByEmail(email) != null)
		{
			return adminDao.findByEmail(email);
		}
		else
		{
			return admin;
		}
	}
}
