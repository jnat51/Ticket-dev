package com.spring.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.dao.CustomerDao;
import com.spring.exception.ErrorException;
import com.spring.model.customer.Customer;
import com.spring.model.customer.CustomerLogin;
import com.spring.model.customer.CustomerWithImage;

@Service
public class CustomerService {
	@Autowired
	CustomerDao customerDao;
	
	public String insertCustomer(Customer customer) throws ErrorException {
		if (customerDao.isCustomerIdExist(customer.getId()) == true) {
			throw new ErrorException("Customer already exist.");
		}
		if (customerDao.isCustomerBkExist(customer.getEmail()) == true) {
			throw new ErrorException("Email already exist!");
		}
		customerDao.saveCustomer(customer);
		return "New customer successfully added";
	}

	public void deleteCustomer(String customerId) throws ErrorException {
		if (customerDao.isCustomerIdExist(customerId) == true) {
			customerDao.deleteCustomer(customerDao.findCustomerById(customerId));
		} else {
			throw new ErrorException("Customer not found!");
		}
	}

	public void updateCustomer(Customer customer) throws ErrorException {
		if (customerDao.isCustomerIdExist(customer.getId()) == false) {
			throw new ErrorException("Customer not found!");
		}
		if (customerDao.isCustomerBkExist(customer.getEmail()) == false) {
			throw new ErrorException("Customer not found!");
		}
		if (!customer.getEmail().equals(customerDao.findCustomerById(customer.getId()).getEmail())) {
			throw new ErrorException("Username cannot be changed!");
		}

		customerDao.saveCustomer(customer);
	}
	
	public Customer findCustomerById(String idCustomer) {
		Customer cust = new Customer();

		if (customerDao.findCustomerById(idCustomer) != null) {
			return customerDao.findCustomerById(idCustomer);
		} else {
			return cust;
		}
	}
	
	public CustomerWithImage findCustomerWithImage(String idCustomer) {
		CustomerWithImage cust = new CustomerWithImage();

		if (customerDao.findByIdWithImage(idCustomer) != null) {
			return customerDao.findByIdWithImage(idCustomer);
		} else {
			return cust;
		}
	}

	public Customer findCustomerByBk(String email) {
		Customer cust = new Customer();

		if (customerDao.findCustomerByBk(email) != null) {
			return customerDao.findCustomerByBk(email);
		} else {
			return cust;
		}
	}
	
	public boolean passwordVerification(String password) throws ErrorException
	{
		if(customerDao.passwordVerification(password) == false) {
			throw new ErrorException("Password is not match.");
		}
		else {
			return true;
		}
	}
	
	public List<Customer> findAll ()
	{
		List<Customer> customers = new ArrayList<Customer>();
		
		if(customerDao.findAll().size() > 0)
		{
			customers = customerDao.findAll();
			
			return customers;
		}
		else
		{
			return customers;
		}
	}
	
	public List<Customer> findWithStatus(String status){
		List<Customer> customers = new ArrayList<Customer>();
		
		if(customerDao.findAllWithStatus(status).size() > 0)
		{
			customers = customerDao.findAllWithStatus(status);
			
			return customers;
		}
		else
		{
			return customers;
		}
	}
	
	public Customer resetPassword(String email) throws ErrorException
	{
		if(customerDao.findByEmail(email) != null)
		{
			return customerDao.findByEmail(email);
		}
		else {
			return null;
		}
	}
}
