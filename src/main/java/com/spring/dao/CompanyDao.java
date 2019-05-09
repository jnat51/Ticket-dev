package com.spring.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.spring.model.Company;
import com.spring.model.Customer;

@Repository
@Transactional
public class CompanyDao extends ParentDao{
	
	//==================================================*Company Dao*==================================================
	public void saveCompany(Company company) {
		super.entityManager.merge(company);
	}
	
	public void deleteCompany(Company company) {
		super.entityManager.remove(company);
	}
	
	public Company findById(String id) {
		try {
			System.out.println("find company by id");
			String query = "from Company where id = :id";
			
			Company company = (Company) this.entityManager
					  .createQuery(query)
					  .setParameter("id",id).getSingleResult();
			
			return company;
			}
			catch(Exception e)
			{
				return null;
			}
	}
	
	public Company findByBk(String companyCode) {
		try {
			System.out.println("find company by bk");
			String query = "from Company where companyCode = :companycode";
			
			Company company = (Company) this.entityManager
					  .createQuery(query)
					  .setParameter("companycode", companyCode).getSingleResult();
			
			return company;
			}
			catch(Exception e)
			{
				return null;
			}
	}
	
	public boolean isIdExist(String id)
	{
		if(findById(id) == null)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	public boolean isBkExist(String companyCode)
	{
		if(findByBk(companyCode) == null)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	//==================================================*Customer Dao*==================================================
	public void saveCustomer(Customer customer) {
		super.entityManager.merge(customer);
	}
	
	public void deleteCustomer(Customer customer) {
		super.entityManager.remove(customer);
	}
}
