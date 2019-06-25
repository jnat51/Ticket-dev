package com.spring.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.spring.enumeration.Enum.Role;
import com.spring.model.User;
import com.spring.model.admin.AdminLogin;
import com.spring.model.agent.AgentLogin;
import com.spring.model.customer.CustomerLogin;

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
	
	public User findByUser(String userId) {
		try {
			String query = "FROM User WHERE user = :userid";
			
			User user = (User) super.entityManager.createQuery(query)
					.setParameter("userid", userId)
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
	
	public AdminLogin loginAdmin(String username) {
		try {
			System.out.println("login");
			String query = "SELECT update_admin.id, tbl_user.username, tbl_user.password, update_admin.name, update_admin.email, update_admin.status, tbl_image.image FROM update_admin " 
					+ "INNER JOIN tbl_user ON update_admin.id = tbl_user.user_id"
					+ "LEFT JOIN tbl_image ON update_admin.image_id = tbl_image.id " + 
					"WHERE tbl_user.username = :username";

			AdminLogin agent = (AdminLogin) super.entityManager.createNativeQuery(query, AgentLogin.class).setParameter("username", username)
					.getSingleResult();

			return agent;
		} catch (Exception e) {
			return null;
		}
	}
	
	public Object login(String username, Role role) {
		try {
			Object object = new Object();
			
			if(role == role.admin) {
			System.out.println("login admin");
				
			String query = "SELECT tbl_admin.id, tbl_user.username, tbl_user.password, tbl_user.role, tbl_admin.name, tbl_admin.email, tbl_admin.status, tbl_image.image FROM tbl_admin " 
					+ "INNER JOIN tbl_user ON tbl_admin.id = tbl_user.user_id "
					+ "LEFT JOIN tbl_image ON tbl_admin.image_id = tbl_image.id "
					+ "WHERE tbl_user.username = :username";

			object = (AdminLogin) super.entityManager.createNativeQuery(query, AdminLogin.class).setParameter("username", username)
					.getSingleResult();
			}
			if(role == role.agent){
				System.out.println("login agent");
				
				String query = "SELECT tbl_agent.id, tbl_user.username, tbl_user.role, tbl_user.password, tbl_agent.name, tbl_agent.email, tbl_agent.status, tbl_image.image FROM tbl_agent " 
						+ "INNER JOIN tbl_user ON tbl_agent.id = tbl_user.user_id "
						+ "LEFT JOIN tbl_image ON tbl_agent.image_id = tbl_image.id "
						+ "WHERE tbl_user.username = :username";
				
				object = (AgentLogin) super.entityManager.createNativeQuery(query, AgentLogin.class).setParameter("username", username)
						.getSingleResult();
			}
			if(role == role.customer) {
				System.out.println("login customer");
				
				String query = "SELECT tbl_customer.id, tbl_customer.name, tbl_customer.company_id, tbl_customer.position, tbl_customer.email, tbl_customer.status, "
						+ "tbl_user.username, tbl_user.password, tbl_user.role, "
						+ "tbl_image.image, "
						+ "tbl_company.company_name, tbl_company.company_code " +
						"FROM tbl_customer "
						+ "INNER JOIN tbl_user ON tbl_customer.id = tbl_user.user_id " + 
						"LEFT JOIN tbl_image ON tbl_customer.image_id = tbl_image.id " +
						"JOIN tbl_company ON tbl_customer.company_id = tbl_company.id " + 
						"WHERE tbl_user.username = :username";

				object = (CustomerLogin) super.entityManager.createNativeQuery(query, CustomerLogin.class).setParameter("username", username)
						.getSingleResult();
			}
			
			return object;
		} catch (Exception e) {
			return null;
		}
	}
}
