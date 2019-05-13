package com.spring.dao;

import org.springframework.stereotype.Repository;

import com.spring.model.Admin;

@Repository
public class AdminDao extends ParentDao{
	public void save(Admin admin) {
		System.out.println("insert admin");
		super.entityManager.merge(admin);
		System.out.println("insert success");
	}
	
	public void delete(Admin admin) {
		this.entityManager.remove(admin);
	}
	
	public Admin findById(String id) {
		try {
			System.out.println("find admin by id");
			String query = "from Admin where id = :id";
			
			Admin admin = (Admin) this.entityManager
					  .createQuery(query)
					  .setParameter("id",id).getSingleResult();
			
			return admin;
			}
			catch(Exception e)
			{
				return null;
			}
	}
	
	public Admin findByBk(String username) {
		try {
			System.out.println("find admin by username");
			String query = "from Admin where username = :username";
			
			Admin admin = (Admin) this.entityManager
					  .createQuery(query)
					  .setParameter("username",username).getSingleResult();
			
			return admin;
			}
			catch(Exception e)
			{
				return null;
			}
	}
	
	public boolean isIdExist(String id) {
		if(findById(id) == null)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	public boolean isBkExist(String username) {
		if(findByBk(username) == null)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	public boolean login(String username, String password)
	{
		try {
			String query = "from Admin where username = :username";
			
			Admin admin = (Admin) this.entityManager
					  .createQuery(query)
					  .setParameter("username",username)
					  .getSingleResult();
			
			System.out.println("Welcome " + admin.getName());
			
			return true;
			}
			catch(Exception e)
			{
				System.out.println("Wrong username/password.");
				
				return false;
			}
	}
}
