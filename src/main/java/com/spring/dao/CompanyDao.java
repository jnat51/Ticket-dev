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
	
	public Company findCompanyById(String id) {
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
	
	public Company findCompanyByBk(String companyCode) {
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
	
	public boolean isCompanyIdExist(String id)
	{
		if(findCompanyById(id) == null)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	public boolean isCompanyBkExist(String companyCode)
	{
		if(findCompanyByBk(companyCode) == null)
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
	
	public Customer findCustomerById(String id) {
		try {
			System.out.println("find customer by id");
			String query = "from Customer where id = :id";
			
			Customer customer = (Customer) this.entityManager
					  .createQuery(query)
					  .setParameter("id",id).getSingleResult();
			
			return customer;
			}
			catch(Exception e)
			{
				return null;
			}
	}
	
	public Customer findCustomerByBk(String username) {
		try {
			System.out.println("find customer by bk");
			String query = "from Customer where username = :username";
			
			Customer customer = (Customer) this.entityManager
					  .createQuery(query)
					  .setParameter("username", username).getSingleResult();
			
			return customer;
			}
			catch(Exception e)
			{
				return null;
			}
	}
	
	public boolean isCustomerIdExist(String id)
	{
		if(findCustomerById(id) == null)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	public boolean isCustomerBkExist(String username)
	{
		if(findCustomerByBk(username) == null)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
}
