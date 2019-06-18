package com.spring.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.spring.model.User;

@Repository
public class UserDao extends ParentDao{
	public void save(User user) {
		super.entityManager.merge(user);
	}
	
	public void delete(User user) {
		super.entityManager.remove(user);
	}
	
	public User findById(String id) {
		try {
		String query = "from User where id = :id";
		
		User user = (User) super.entityManager.createQuery(query)
				.setParameter("id", id)
				.getSingleResult();
		
		return user;
		}catch(Exception e) {
			return null;
		}
	}
	
	public User findByBk(String username) {
		try {
			String query = "from User where username = :username";
			
			User user = (User) super.entityManager.createQuery(query)
					.setParameter("username", username)
					.getSingleResult();
			
			return user;
		}catch(Exception e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<User> findAll() {
		try {
			String query = "from User";
			
			List<User> users = super.entityManager.createQuery(query)
					.getResultList();
			
			return users;
		}catch(Exception e) {
			return null;
		}
	}
	
	public boolean isIdExist(String id) {
		if (findById(id) == null) {
			return false;
		} else {
			return true;
		}
	}

	public boolean isBkExist(String username) {
		if (findByBk(username) == null) {
			return false;
		} else {
			return true;
		}
	}
}
