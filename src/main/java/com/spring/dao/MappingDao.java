package com.spring.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.spring.model.Mapping;

@Repository
@Transactional
public class MappingDao extends ParentDao {
	public void save(Mapping mapping) {
		super.entityManager.merge(mapping);
	}
	
	public void delete(Mapping mapping) {
		super.entityManager.remove(mapping);
	}
	
	public Mapping findById(String id) {
		try {
			String query = "from Mapping where id = :id";
			
			Mapping mapping = (Mapping) this.entityManager
					  .createQuery(query)
					  .setParameter("id",id).getSingleResult();
			
			return mapping;
			}
			catch(Exception e)
			{
				return null;
			}
	}
	
	public Mapping findByBk(String companyId) {
		try {
			String query = "from Mapping where company.id = :companyid";
			
			Mapping mapping = (Mapping) this.entityManager
					  .createQuery(query)
					  .setParameter("companyid", companyId).getSingleResult();
			
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
