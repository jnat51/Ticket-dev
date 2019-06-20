package com.spring.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.spring.model.admin.AdminLogin;
import com.spring.model.admin.AdminUpdate;
import com.spring.model.agent.AgentLogin;

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
	
	public AdminLogin login(String username) {
		try {
			System.out.println("login");
			String query = "SELECT update_admin.id, tbl_user.username, tbl_user.password, update_admin.name, update_admin.email, update_admin.status, tbl_image.image FROM update_admin " 
					+ "INNER JOIN tbl_user ON update_admin.id = tbl_user.user_id"
					+ "LEFT JOIN tbl_image ON update_admin.image_id = tbl_image.id " + 
					"WHERE update_admin.username = :username";

			AdminLogin agent = (AdminLogin) super.entityManager.createNativeQuery(query, AgentLogin.class).setParameter("username", username)
					.getSingleResult();

			return agent;
		} catch (Exception e) {
			return null;
		}
	}
}
