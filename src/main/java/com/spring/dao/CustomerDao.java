package com.spring.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.spring.model.Customer;

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
	
	public Customer findCustomerByBk(String username) {
		try {
			System.out.println("find customer by bk");
			String query = "from Customer where username = :username";
			
			Customer customer = (Customer) super.entityManager
					  .createQuery(query)
					  .setParameter("username", username).getSingleResult();
			
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
}
