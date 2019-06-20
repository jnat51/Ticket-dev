package com.spring.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.dao.UserDao;
import com.spring.exception.ErrorException;
import com.spring.model.User;

@Service
public class UserService {
	@Autowired
	UserDao userDao;
	
	public void delete(String id) throws ErrorException
	{
		if(userDao.isIdExist(id) == true)
		{
			userDao.delete(userDao.findById(id));
		}
		else
		{
			throw new ErrorException("User not found!");
		}
	}
	
	public String insert(User user) throws ErrorException
	{
		if(userDao.isIdExist(user.getId()) == true)
		{
			throw new ErrorException("User already exist.");
		}
		if(userDao.isBkExist(user.getUsername()) == true)
		{
			throw new ErrorException("Username already exist!");
		}
		
			userDao.save(user);
			return "New User account successfully created";
	}
	
	public void update(User user) throws ErrorException
	{
		if(userDao.isIdExist(user.getId()) == false)
		{
			throw new ErrorException("User not found!");
		}
		if(userDao.isBkExist(user.getUsername()) == false)
		{
			throw new ErrorException("User not found!");
		}
		if(!user.getUsername().equals(userDao.findById(user.getId()).getUsername()))
		{
			throw new ErrorException("Username cannot be changed!");
		}
		userDao.save(user);
	}
	
	public User findByBk(String username)
	{
		User user = new User();
		
		if(userDao.findByBk(username) != null)
		{
			user = userDao.findByBk(username);
			
			return user;
		}
		else
		{
			return user;
		}
	}
	
	public User findByUserId(String userId)
	{
		User user = new User();
		
		if(userDao.findByUser(userId) != null)
		{
			user = userDao.findByUser(userId);
			
			return user;
		}
		else
		{
			return user;
		}
	}
	
	public List<User> findAll ()
	{
		List<User> users = new ArrayList<User>();
		
		if(userDao.findAll().size() > 0)
		{
			users = userDao.findAll();
			
			return users;
		}
		else
		{
			return users;
		}
	}
	
	public User findById(String id)
	{
		User user = new User();
		
		if(userDao.findById(id) != null)
		{
			return userDao.findById(id);
		}
		else
		{
			return user;
		}
	}
}
