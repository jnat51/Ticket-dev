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
	
	public void deleteCompany(String companyId) throws ServiceException
	{
		if(companyDao.isCompanyIdExist(companyId) == true)
		{
			companyDao.deleteCompany(companyDao.findCompanyById(companyId));
		}
		else
		{
			throw new ServiceException("Agent not found!");
		}
	}
	
	public String insertCompany(Company company) throws ServiceException
	{
		if(companyDao.isCompanyIdExist(company.getId()) == true)
		{
			throw new ServiceException("Agent already exist.");
		}
		if(companyDao.isCompanyBkExist(company.getCompanyCode()) == true)
		{
			throw new ServiceException("Email already exist!");
		}
			companyDao.saveCompany(company);
			return "New company successfully added";
	}
	
	public Company findCompanyById(String idCompany)
	{
		Company comp = new Company();
		
		if(companyDao.findCompanyById(idCompany) != null)
		{
			return companyDao.findCompanyById(idCompany);
		}
		else
		{
			return comp;
		}
	}
	
	public Company findCompanyByBk(String companyCode)
	{
		Company comp = new Company();
		
		if(companyDao.findCompanyByBk(companyCode) != null)
		{
			return companyDao.findCompanyByBk(companyCode);
		}
		else
		{
			return comp;
		}
	}
	
//====================================*Customer Service*===============================================	
	
	public String insertCustomer(Customer customer) throws ServiceException
	{
			companyDao.saveCustomer(customer);
			return "New customer successfully added";
	}
}
