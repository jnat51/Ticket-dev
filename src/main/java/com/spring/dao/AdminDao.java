package com.spring.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.spring.model.admin.Admin;
import com.spring.model.admin.AdminLogin;

@Repository
public class AdminDao extends ParentDao {
	public void save(Admin admin) {
		System.out.println("merge admin");
		super.entityManager.merge(admin);
		System.out.println("merge success");
	}

	public void delete(Admin admin) {
		this.entityManager.remove(admin);
	}

	public Admin findById(String id) {
		try {
			System.out.println("find admin by id");
			String query = "from Admin where id = :id";

			Admin admin = (Admin) super.entityManager.createQuery(query).setParameter("id", id).getSingleResult();

			return admin;
		} catch (Exception e) {
			return null;
		}
	}
	
	public List<Admin> findAll (){
		try {
			String query = "from Admin";
			
			List<Admin> admins = new ArrayList<Admin>();
			
			admins = super.entityManager.createQuery(query).getResultList();

			return admins;
		}
		catch (Exception e) {
			return null;
		}
	}

	public Admin findByBk(String username) {
		try {
			System.out.println("find admin by username");
			String query = "from Admin where username = :username";

			Admin admin = (Admin) super.entityManager.createQuery(query).setParameter("username", username)
					.getSingleResult();

			return admin;
		} catch (Exception e) {
			return null;
		}
	}
	
	public AdminLogin login(String username) {
		try {
			System.out.println("login");
			String query = "SELECT tbl_admin.id, tbl_admin.username, tbl_admin.password, tbl_admin.name, tbl_admin.email, tbl_image.image FROM tbl_admin " + 
					"LEFT JOIN tbl_image ON tbl_admin.image_id = tbl_image.id " + 
					"WHERE tbl_admin.username = :username";

			AdminLogin admin = (AdminLogin) super.entityManager.createNativeQuery(query, AdminLogin.class).setParameter("username", username)
					.getSingleResult();

			return admin;
		} catch (Exception e) {
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
	
	public boolean isEmailExist(String email) {
		if (findByEmail(email) == null) {
			return false;
		} else {
			return true;
		}
	}

	public boolean passwordVerification(String password) {
		try {
			String query = "from Admin where password = :password";

			Admin admin = (Admin) super.entityManager.createQuery(query).setParameter("password", password)
					.getSingleResult();

			if (admin.getId() == null) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			System.out.println("Wrong password.");

			return false;
		}
	}

	public Admin findByEmail(String email) {
		try {
			String query = "from Admin WHERE email = :email";
			
			Admin admin = (Admin) super.entityManager.createQuery(query).setParameter("email", email)
					.getSingleResult();
			
			return admin;
		}
		catch (Exception e) {
			return null;
		}
	}
}
