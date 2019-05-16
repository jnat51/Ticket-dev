package com.spring.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.spring.model.Mapping;

@Repository
@Transactional
public class MappingDao extends ParentDao {
	public void save(Mapping pic) {
		super.entityManager.merge(pic);
	}
	
	public void delete(Mapping pic) {
		super.entityManager.remove(pic);
	}
	
	public Mapping findById(String id) {
		try {
			String query = "from Pic where id = :id";
			
			Mapping pic = (Mapping) this.entityManager
					  .createQuery(query)
					  .setParameter("id",id).getSingleResult();
			
			return pic;
			}
			catch(Exception e)
			{
				return null;
			}
	}
	
	public Mapping findByBk(String companyId) {
		try {
			String query = "from Pic where company.id = :companyid";
			
			Mapping pic = (Mapping) this.entityManager
					  .createQuery(query)
					  .setParameter("companyid", companyId).getSingleResult();
			
			return pic;
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
