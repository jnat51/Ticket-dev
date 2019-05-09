package com.spring.service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.dao.AgentDao;
import com.spring.model.Agent;

@Service
public class AgentService {
	@Autowired
	AgentDao agentDao;
	
	public void delete(String agentId) throws ServiceException
	{
		if(agentDao.isIdExist(agentId) == true)
		{
			agentDao.delete(agentDao.findById(agentId));
		}
		else
		{
			throw new ServiceException("Agent not found!");
		}
	}
	
	public String insert(Agent agent) throws ServiceException
	{
		if(agentDao.isIdExist(agent.getId()) == true)
		{
			throw new ServiceException("Agent already exist.");
		}
		if(agentDao.isBkExist(agent.getEmail(), agent.getUsername()) == true)
		{
			throw new ServiceException("Email already exist!");
		}
			agentDao.save(agent);
			return "New agent account successfully created";
	}
	
	public void update(Agent agent) throws ServiceException
	{
		if(agentDao.isIdExist(agent.getId()) == false)
		{
			throw new ServiceException("Agent not found!");
		}
		if(agentDao.isBkExist(agent.getUsername(), agent.getUsername()) == false)
		{
			throw new ServiceException("Agent not found!");
		}
		if(!agent.getUsername().equals(agentDao.findById(agent.getId()).getUsername()))
		{
			throw new ServiceException("Username cannot be changed!");
		}
		agentDao.save(agent);
	}
	
	public Agent findByBk(String email, String username)
	{
		Agent agent = new Agent();
		
		if(agentDao.findByBk(email, username) != null)
		{
			agent = agentDao.findByBk(email, username);
			
			return agent;
		}
		else
		{
			return agent;
		}
	}
	
	public Agent findUsername(String username)
	{
		Agent agent = new Agent();
		
		if(agentDao.findUsername(username) != null)
		{
			agent = agentDao.findUsername(username);
			
			return agent;
		}
		else
		{
			return agent;
		}
	}
	
	public Agent findById(String idAgent)
	{
		Agent agent = new Agent();
		
		if(agentDao.findById(idAgent) != null)
		{
			return agentDao.findById(idAgent);
		}
		else
		{
			return agent;
		}
	}
	
	public List<Agent> findByFilter(String namaAgent, String username)
	{
		List<Agent> barangs = new ArrayList<Agent>();
		
		if(agentDao.findByFilter(namaAgent, username).size() > 0)
		{
			barangs = agentDao.findByFilter(namaAgent, username);
			
			return barangs;
		}
		else
		{
			return barangs;
		}
	}
}
