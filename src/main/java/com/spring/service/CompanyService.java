package com.spring.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.dao.CompanyDao;
import com.spring.exception.ErrorException;
import com.spring.model.Company;
import com.spring.model.Customer;

@Service
public class CompanyService {
	@Autowired
	CompanyDao companyDao;

	public void deleteCompany(String companyId) throws ErrorException {
		if (companyDao.isCompanyIdExist(companyId) == true) {
			companyDao.deleteCompany(companyDao.findCompanyById(companyId));
		} else {
			throw new ErrorException("Company not found!");
		}
	}

	public String insertCompany(Company company) throws ErrorException {
		if (companyDao.isCompanyIdExist(company.getId()) == true) {
			throw new ErrorException("Company already exist.");
		}
		if (companyDao.isCompanyBkExist(company.getCompanyCode()) == true) {
			throw new ErrorException("Company code already exist!");
		}
		companyDao.saveCompany(company);
		return "New company successfully added";
	}

	public void updateCompany(Company company) throws ErrorException {
		if (companyDao.isCompanyIdExist(company.getId()) == false) {
			throw new ErrorException("Company not found!");
		}
		if (companyDao.isCompanyBkExist(company.getCompanyCode()) == false) {
			throw new ErrorException("Company not found!");
		}
		if (!company.getCompanyCode().equals(companyDao.findCompanyById(company.getId()).getCompanyCode())) {
			throw new ErrorException("Company code cannot be changed!");
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
	
	public List<Company> findAll ()
	{
		List<Company> company = new ArrayList<Company>();
		
		if(companyDao.findAll().size() > 0)
		{
			company = companyDao.findAll();
			
			return company;
		}
		else
		{
			return company;
		}
	}
	
	public List<Company> findWithStatus (String status)
	{
		List<Company> companies = new ArrayList<Company>();
		
		if(companyDao.findAllWithStatus(status).size() > 0)
		{
			companies = companyDao.findAllWithStatus(status);
			
			return companies;
		}
		else
		{
			return companies;
		}
	}
}
