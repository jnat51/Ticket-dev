package com.spring.service;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.dao.CompanyDao;
import com.spring.model.Company;
import com.spring.model.Customer;

@Service
public class CompanyService {
	@Autowired
	CompanyDao companyDao;

	public void deleteCompany(String companyId) throws ServiceException {
		if (companyDao.isCompanyIdExist(companyId) == true) {
			companyDao.deleteCompany(companyDao.findCompanyById(companyId));
		} else {
			throw new ServiceException("Company not found!");
		}
	}

	public String insertCompany(Company company) throws ServiceException {
		if (companyDao.isCompanyIdExist(company.getId()) == true) {
			throw new ServiceException("Company already exist.");
		}
		if (companyDao.isCompanyBkExist(company.getCompanyCode()) == true) {
			throw new ServiceException("Company code already exist!");
		}
		companyDao.saveCompany(company);
		return "New company successfully added";
	}

	public void updateCompany(Company company) throws ServiceException {
		if (companyDao.isCompanyIdExist(company.getId()) == false) {
			throw new ServiceException("Company not found!");
		}
		if (companyDao.isCompanyBkExist(company.getCompanyCode()) == false) {
			throw new ServiceException("Company not found!");
		}
		if (!company.getCompanyCode().equals(companyDao.findCompanyById(company.getId()).getCompanyCode())) {
			throw new ServiceException("Company code cannot be changed!");
		}

		companyDao.saveCompany(company);
	}

	public Company findCompanyById(String idCompany) {
		Company comp = new Company();

		if (companyDao.findCompanyById(idCompany) != null) {
			return companyDao.findCompanyById(idCompany);
		} else {
			return comp;
		}
	}

	public Company findCompanyByBk(String companyCode) {
		Company comp = new Company();

		if (companyDao.findCompanyByBk(companyCode) != null) {
			return companyDao.findCompanyByBk(companyCode);
		} else {
			return comp;
		}
	}

//====================================*Customer Service*===============================================	

	public String insertCustomer(Customer customer) throws ServiceException {
		companyDao.saveCustomer(customer);
		return "New customer successfully added";
	}

	public void deleteCustomer(String customerId) throws ServiceException {
		if (companyDao.isCompanyIdExist(customerId) == true) {
			companyDao.deleteCustomer(companyDao.findCustomerById(customerId));
		} else {
			throw new ServiceException("Customer not found!");
		}
	}

	public void updateCustomer(Customer customer) throws ServiceException {
		if (companyDao.isCompanyIdExist(customer.getId()) == false) {
			throw new ServiceException("Customer not found!");
		}
		if (companyDao.isCompanyBkExist(customer.getUsername()) == false) {
			throw new ServiceException("Customer not found!");
		}
		if (!customer.getUsername().equals(companyDao.findCustomerById(customer.getId()).getUsername())) {
			throw new ServiceException("Username cannot be changed!");
		}

		companyDao.saveCustomer(customer);
	}
	
	public Customer findCustomerById(String idCustomer) {
		Customer cust = new Customer();

		if (companyDao.findCustomerById(idCustomer) != null) {
			return companyDao.findCustomerById(idCustomer);
		} else {
			return cust;
		}
	}

	public Customer findCustomerByBk(String username) {
		Customer cust = new Customer();

		if (companyDao.findCustomerByBk(username) != null) {
			return companyDao.findCustomerByBk(username);
		} else {
			return cust;
		}
	}
}
