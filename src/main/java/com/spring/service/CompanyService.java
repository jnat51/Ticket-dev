package com.spring.service;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.dao.CompanyDao;
import com.spring.model.Company;

@Service
public class CompanyService {
	@Autowired
	CompanyDao companyDao;
	
	public void deleteCompany(String companyId) throws ServiceException
	{
		if(companyDao.isIdExist(companyId) == true)
		{
			companyDao.deleteCompany(companyDao.findById(companyId));
		}
		else
		{
			throw new ServiceException("Agent not found!");
		}
	}
	
	public String insertCompany(Company company) throws ServiceException
	{
		if(companyDao.isIdExist(company.getId()) == true)
		{
			throw new ServiceException("Agent already exist.");
		}
		if(companyDao.isBkExist(company.getCompanyCode()) == true)
		{
			throw new ServiceException("Email already exist!");
		}
			companyDao.saveCompany(company);
			return "New company successfully added";
	}
	
}
