package com.spring.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.dao.CustomerUpdateDao;
import com.spring.exception.ErrorException;
import com.spring.model.customer.Customer;
import com.spring.model.customer.CustomerUpdate;

@Service
public class CustomerUpdateService {
	@Autowired
	CustomerUpdateDao customerUpdateDao;
	
	public String insertCustomer(CustomerUpdate customerUpdate) throws ErrorException {
		if (customerUpdateDao.isCustomerIdExist(customerUpdate.getId()) == true) {
			throw new ErrorException("Customer already exist.");
		}
		if (customerUpdateDao.isCustomerBkExist(customerUpdate.getEmail()) == true) {
			throw new ErrorException("Email already exist!");
		}
		customerUpdateDao.saveCustomer(customerUpdate);
		return "New customer successfully added";
	}

	public void deleteCustomer(String customerId) throws ErrorException {
		if (customerUpdateDao.isCustomerIdExist(customerId) == true) {
			customerUpdateDao.deleteCustomer(customerUpdateDao.findCustomerById(customerId));
		} else {
			throw new ErrorException("Customer not found!");
		}
	}

	public void updateCustomer(CustomerUpdate customerUpdate) throws ErrorException {
		if (customerUpdateDao.isCustomerIdExist(customerUpdate.getId()) == false) {
			throw new ErrorException("Customer not found!");
		}
		if (customerUpdateDao.isCustomerBkExist(customerUpdate.getEmail()) == false) {
			throw new ErrorException("Customer not found!");
		}
		if (!customerUpdate.getEmail().equals(customerUpdateDao.findCustomerById(customerUpdate.getId()).getEmail())) {
			throw new ErrorException("Username cannot be changed!");
		}

		customerUpdateDao.saveCustomer(customerUpdate);
	}
	
	public CustomerUpdate findCustomerById(String idCustomer) {
		CustomerUpdate cust = new CustomerUpdate();

		if (customerUpdateDao.findCustomerById(idCustomer) != null) {
			return customerUpdateDao.findCustomerById(idCustomer);
		} else {
			return cust;
		}
	}
	
	public CustomerUpdate findCustomerByBk(String email) {
		CustomerUpdate cust = new CustomerUpdate();

		if (customerUpdateDao.findCustomerByBk(email) != null) {
			return customerUpdateDao.findCustomerByBk(email);
		} else {
			return cust;
		}
	}
	
	public List<CustomerUpdate> findWithStatus(String status){
		List<CustomerUpdate> customers = new ArrayList<CustomerUpdate>();
		
		if(customerUpdateDao.findAllWithStatus(status).size() > 0)
		{
			customers = customerUpdateDao.findAllWithStatus(status);
			
			return customers;
		}
		else
		{
			return customers;
		}
	}
}
