package com.spring.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.spring.model.agent.AgentLogin;
import com.spring.model.agent.AgentWithImage;
import com.spring.model.customer.Customer;
import com.spring.model.customer.CustomerLogin;
import com.spring.model.customer.CustomerWithImage;

@Repository
@Transactional
public class CustomerDao extends ParentDao{
	public void saveCustomer(Customer customer) {
		super.entityManager.merge(customer);
	}
	
	public void deleteCustomer(Customer customer) {
		super.entityManager.remove(customer);
	}
	
	public Customer findCustomerById(String id) {
		try {
			System.out.println("find customer by id");
			String query = "from Customer where id = :id";
			
			Customer customer = (Customer) super.entityManager
					  .createQuery(query)
					  .setParameter("id",id).getSingleResult();
			
			return customer;
			}
			catch(Exception e)
			{
				return null;
			}
	}
	
	public CustomerWithImage findByIdWithImage(String id)
	{
		try {
			String query = "SELECT tbl_customer.*, tbl_image.image "
					+ "FROM tbl_customer "
					+ "JOIN tbl_image ON tbl_customer.image_id = tbl_image.id "
					+ "WHERE tbl_customer.id = :id";
			
			CustomerWithImage customer =  (CustomerWithImage) super.entityManager
					  .createNativeQuery(query)
					  .setParameter("id",id).getResultList().get(0);
			
			return customer;
			}
			catch(Exception e)
			{
				return null;
			}
	}
	
	public Customer findCustomerByBk(String email) {
		try {
			System.out.println("find customer by bk");
			String query = "from Customer where email = :email";
			
			Customer customer = (Customer) super.entityManager
					  .createQuery(query)
					  .setParameter("email", email).getSingleResult();
			
			return customer;
			}
			catch(Exception e)
			{
				return null;
			}
	}
	
	public boolean isCustomerIdExist(String id)
	{
		if(findCustomerById(id) == null)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	public boolean isCustomerEmailExist(String email)
	{
		if(findByEmail(email)==null)
		{
			return false;
		}
		else {
			return true;
		}
	}
	
	public boolean isCustomerBkExist(String username)
	{
		if(findCustomerByBk(username) == null)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	public boolean passwordVerification(String password) {
		try {
			String query = "from Customer where password = :password";

			Customer customer = (Customer) super.entityManager.createQuery(query).setParameter("password", password)
					.getSingleResult();

			if (customer.getId() == null) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			System.out.println("Wrong password.");

			return false;
		}
	}

	public Customer findByEmail(String email) {
		try {
			String query = "from Customer WHERE email = :email";
			
			Customer customer = (Customer) super.entityManager.createQuery(query).setParameter("email", email)
					.getSingleResult();
			
			return customer;
		}
		catch (Exception e) {
			return null;
		}
	}
	
	public CustomerWithImage findWithImage(String id)
	{
		try {
			String query = "SELECT tbl_customer.id, tbl_customer.username, tbl_customer.password, tbl_customer.name, tbl_customer.company_id, tbl_customer.position, tbl_customer.email, tbl_customer.status, "
					+ "tbl_image.image, "
					+ "tbl_company.company_name, tbl_company.company_code " +
					"FROM tbl_customer " + 
					"LEFT JOIN tbl_image ON tbl_customer.image_id = tbl_image.id " +
					"JOIN tbl_company ON tbl_customer.company_id = tbl_company.id " + 
					"WHERE tbl_customer.username = :username";
			
			CustomerWithImage customer = (CustomerWithImage) super.entityManager
					  .createNativeQuery(query)
					  .setParameter("id",id).getSingleResult();
			
			return customer;
			}
			catch(Exception e)
			{
				return null;
			}
	}
	
	public List<Customer> findAll (){
		try {
			String query = "from Customer";
			
			List<Customer> customers = new ArrayList<Customer>();
			
			customers = super.entityManager.createQuery(query).getResultList();

			return customers;
		}
		catch (Exception e) {
			return null;
		}
	}
	
	public List<Customer> findAllWithStatus(String status){
		try {
			String query = "SELECT * FROM tbl_customer WHERE status = :status";
			
			List<Customer> customers = new ArrayList<Customer>();
			
			customers = super.entityManager.createNativeQuery(query, Customer.class)
					.setParameter("status", status)
					.getResultList();

			return customers;
		}catch(Exception e){
			return null;
		}
	}
}
