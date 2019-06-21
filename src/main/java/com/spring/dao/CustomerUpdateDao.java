package com.spring.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.spring.model.customer.Customer;
import com.spring.model.customer.CustomerUpdate;

@Repository
public class CustomerUpdateDao extends ParentDao{
	public void saveCustomer(CustomerUpdate customerUpdater) {
		super.entityManager.merge(customerUpdater);
	}
	
	public void deleteCustomer(CustomerUpdate customerUpdater) {
		super.entityManager.remove(customerUpdater);
	}
	
	public CustomerUpdate findCustomerById(String id) {
		try {
			System.out.println("find customer by id");
			String query = "from CustomerUpdate where id = :id";
			
			CustomerUpdate customer = (CustomerUpdate) super.entityManager
					  .createQuery(query)
					  .setParameter("id",id).getSingleResult();
			
			return customer;
			}
			catch(Exception e)
			{
				return null;
			}
	}
	
	public CustomerUpdate findCustomerByBk(String email) {
		try {
			System.out.println("find customer by bk");
			String query = "from CustomerUpdate where email = :email";
			
			CustomerUpdate customer = (CustomerUpdate) super.entityManager
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
	
	public boolean isCustomerBkExist(String email)
	{
		if(findCustomerByBk(email) == null)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	public List<CustomerUpdate> findAllWithStatus(String status){
		try {
			String query = "SELECT * FROM update_customer WHERE status = :status";
			
			List<CustomerUpdate> customers = new ArrayList<CustomerUpdate>();
			
			customers = super.entityManager.createNativeQuery(query, Customer.class)
					.setParameter("status", status)
					.getResultList();

			return customers;
		}catch(Exception e){
			return null;
		}
	}
}
