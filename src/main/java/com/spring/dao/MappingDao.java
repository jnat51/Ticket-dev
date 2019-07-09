package com.spring.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.spring.model.Mapping;
import com.spring.model.MappingReport;
import com.spring.model.customer.Customer;

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
		Mapping mapping;
		try {
			String query = "from Mapping where company.id = :companyid";
			
			mapping = (Mapping) super.entityManager
					  .createQuery(query)
					  .setParameter("companyid", companyId).getSingleResult();
			
			return mapping;
			}
			catch(Exception e)
			{
				return null;
			}
	}
	
	@SuppressWarnings("unchecked")
	public List<MappingReport> findMappingWithLogo() {
		List<MappingReport> mappings;
		try {

			String query = "SELECT tbl_mapping.id, tbl_company.company_code, tbl_company.company_name, tbl_company.status, tbl_agent.name, tbl_image.image "
					+ "FROM tbl_mapping "
					+ "LEFT JOIN tbl_company ON tbl_mapping.company_id = tbl_company.id "
					+ "LEFT JOIN tbl_agent ON tbl_mapping.agent_id = tbl_agent.id "
					+ "LEFT JOIN tbl_image ON tbl_company.image_id = tbl_image.id";

			mappings = super.entityManager.createNativeQuery(query, MappingReport.class).getResultList();			

			return mappings;
		} catch (Exception e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<MappingReport> getAllWithStatus() {
		List<MappingReport> mappings;
		try {

			String query = "SELECT tbl_mapping.id, tbl_company.company_code, tbl_company.company_name, tbl_company.status, tbl_agent.name, tbl_image.image "
					+ "FROM tbl_mapping "
					+ "JOIN tbl_company ON tbl_mapping.company_id = tbl_company.id "
					+ "JOIN tbl_agent ON tbl_mapping.agent_id = tbl_agent.id "
					+ "JOIN tbl_image ON tbl_company.image_id = tbl_image.id "
					+ "WHERE tbl_company.status = 'active'";

			mappings = super.entityManager.createNativeQuery(query, MappingReport.class).getResultList();			

			return mappings;
		} catch (Exception e) {
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
