package com.spring.service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.dao.MappingDao;
import com.spring.model.Mapping;
import com.spring.model.MappingReport;

@Service
public class MappingService {
	@Autowired
	MappingDao mappingDao;
	
	public void insert(Mapping pic) {
		if(mappingDao.isIdExist(pic.getId()) == true)
		{
			throw new ServiceException("Pic already exist.");
		}
		if(mappingDao.isBkExist(pic.getCompany().getId()) == true)
		{
			throw new ServiceException("Pic already exist!");
		}
		mappingDao.save(pic);
	}
	
	public void delete(String id) throws ServiceException
	{
		if(mappingDao.isIdExist(id) == true)
		{
			mappingDao.delete(mappingDao.findById(id));
		}
		else
		{
			throw new ServiceException("Pic not found!");
		}
	}
	
	public void update(Mapping pic) throws ServiceException
	{
		if(mappingDao.isIdExist(pic.getId()) == false)
		{
			throw new ServiceException("Pic not found!");
		}
		if(mappingDao.isBkExist(pic.getCompany().getId()) == false)
		{
			throw new ServiceException("Pic not found!");
		}
		if(!pic.getCompany().getId().equals(mappingDao.findById(pic.getId()).getCompany().getId()))
		{
			throw new ServiceException("Username cannot be changed!");
		}
		mappingDao.save(pic);
	}
	
	public Mapping findById(String id)
	{
		Mapping pic = new Mapping();
		
		if(mappingDao.findById(id) != null)
		{
			return mappingDao.findById(id);
		}
		else
		{
			return pic;
		}
	}
	
	public Mapping findByBk(String companyId)
	{
		Mapping pic = new Mapping();
		
		if(mappingDao.findByBk(companyId) != null)
		{
			return mappingDao.findByBk(companyId);
		}
		else
		{
			return pic;
		}
	}
	
	public List<MappingReport> findWithCompanyLogo(){
		List<MappingReport> maps = new ArrayList<MappingReport>();
		
		if(mappingDao.findMappingWithLogo() != null)
		{
			return mappingDao.findMappingWithLogo();
		}
		else
		{
			return maps;
		}
	}
}
