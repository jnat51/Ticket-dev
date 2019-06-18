package com.spring.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.spring.model.admin.AdminUpdate;

@Repository
public class AdminUpdateDao extends ParentDao{
	public void save(AdminUpdate adminUpdate) {
		super.entityManager.merge(adminUpdate);
	}
	
	public void delete(AdminUpdate adminUpdate) {
		super.entityManager.remove(adminUpdate);
	}
	
	public AdminUpdate findById(String id) {
		try {
		String query = "from AdminUpdate where id = :id";
		
		AdminUpdate adminUpdate = (AdminUpdate) super.entityManager.createQuery(query)
				.setParameter("id", id)
				.getSingleResult();
		
		return adminUpdate;
		}catch(Exception e) {
			return null;
		}
	}
	
	public AdminUpdate findByBk(String email) {
		try {
			String query = "from AdminUpdate where email = :email";
			
			AdminUpdate adminUpdate = (AdminUpdate) super.entityManager.createQuery(query)
					.setParameter("email", email)
					.getSingleResult();
			
			return adminUpdate;
		}catch(Exception e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<AdminUpdate> findAll() {
		try {
			String query = "from AdminUpdate";
			
			List<AdminUpdate> adminUpdate = super.entityManager.createQuery(query)
					.getResultList();
			
			return adminUpdate;
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
