package com.spring.service;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.dao.CustomerDao;
import com.spring.model.Customer;

@Service
public class CustomerService {
	@Autowired
	CustomerDao customerDao;
	
	public String insertCustomer(Customer customer) throws ServiceException {
		customerDao.saveCustomer(customer);
		return "New customer successfully added";
	}

	public void deleteCustomer(String customerId) throws ServiceException {
		if (customerDao.isCustomerIdExist(customerId) == true) {
			customerDao.deleteCustomer(customerDao.findCustomerById(customerId));
		} else {
			throw new ServiceException("Customer not found!");
		}
	}

	public void updateCustomer(Customer customer) throws ServiceException {
		if (customerDao.isCustomerIdExist(customer.getId()) == false) {
			throw new ServiceException("Customer not found!");
		}
		if (customerDao.isCustomerBkExist(customer.getUsername()) == false) {
			throw new ServiceException("Customer not found!");
		}
		if (!customer.getUsername().equals(customerDao.findCustomerById(customer.getId()).getUsername())) {
			throw new ServiceException("Username cannot be changed!");
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
}
