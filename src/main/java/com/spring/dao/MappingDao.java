package com.spring.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.spring.model.Customer;
import com.spring.model.Mapping;

@Repository
@Transactional
public class MappingDao extends ParentDao {
	public void save(Mapping mapping) {
		System.out.println("Merge mapping");
		super.entityManager.merge(mapping);
		System.out.println("Merge success");
	}
	
	public void delete(Mapping mapping) {
		super.entityManager.remove(mapping);
	}
	
	public Mapping findById(String id) {
		Mapping mapping;
		try {
			String query = "from Mapping where id = :id";
			
			 mapping = (Mapping) super.entityManager
					  .createNativeQuery(query)
					  .setParameter("id",id).getSingleResult();
			 
			 List<Customer> customers = new ArrayList<Customer>();
			 for (Customer cust : mapping.getCompany().getCustomers()) {
					cust.setCompany(null);
					customers.add(cust);
				}
			 
			 mapping.getCompany().setCustomers(customers);
			
			return mapping;
			}
			catch(Exception e)
			{
				return null;
			}
	}
	
	public Mapping findByBk(String companyId) {
		Mapping mapping;
		try {
			String query = "from Mapping where company.id = :companyid";
			
			mapping = (Mapping) super.entityManager
					  .createQuery(query)
					  .setParameter("companyid", companyId).getSingleResult();
			
			List<Customer> customers = new ArrayList<Customer>();
			 for (Customer cust : mapping.getCompany().getCustomers()) {
					cust.setCompany(null);
					customers.add(cust);
				}
			 
			 mapping.getCompany().setCustomers(customers);
			
			return mapping;
			}
			catch(Exception e)
			{
				return null;
			}
	}
	
	public boolean isIdExist(String id) {
		if(findById(id) == null)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	public boolean isBkExist(String companyId) {
		if(findByBk(companyId) == null)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
}
