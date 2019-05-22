package com.spring.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.dao.CustomerDao;
import com.spring.exception.ErrorException;
import com.spring.model.Admin;
import com.spring.model.Agent;
import com.spring.model.Customer;

@Service
public class CustomerService {
	@Autowired
	CustomerDao customerDao;
	
	public String insertCustomer(Customer customer) throws ErrorException {
		if (customerDao.isCustomerIdExist(customer.getId()) == true) {
			throw new ErrorException("Customer already exist.");
		}
		if (customerDao.isCustomerBkExist(customer.getUsername()) == true) {
			throw new ErrorException("Username already exist!");
		}
		if(customerDao.isCustomerEmailExist(customer.getEmail()) == true) {
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
		if (customerDao.isCustomerBkExist(customer.getUsername()) == false) {
			throw new ErrorException("Customer not found!");
		}
		if (!customer.getUsername().equals(customerDao.findCustomerById(customer.getId()).getUsername())) {
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

	public Customer findCustomerByBk(String username) {
		Customer cust = new Customer();

		if (customerDao.findCustomerByBk(username) != null) {
			return customerDao.findCustomerByBk(username);
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
	
	public Customer login(String username) throws ErrorException
	{
		Customer customer = new Customer();
		
		if(customerDao.findCustomerByBk(username) == null) {
			throw new ErrorException("Wrong username/password!");
		}
		else {
			return customer;
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
