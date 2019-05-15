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
}
