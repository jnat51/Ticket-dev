package com.spring.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.dao.AgentUpdateDao;
import com.spring.exception.ErrorException;
import com.spring.model.agent.AgentUpdate;

@Service
public class AgentUpdateService {
	@Autowired
	AgentUpdateDao agentUpdateDao;
	
	public void delete(String agentId) throws ErrorException
	{
		if(agentUpdateDao.isIdExist(agentId) == true)
		{
			agentUpdateDao.delete(agentUpdateDao.findById(agentId));
		}
		else
		{
			throw new ErrorException("Agent not found!");
		}
	}
	
	public String insert(AgentUpdate agentUpdate) throws ErrorException
	{
		if(agentUpdateDao.isIdExist(agentUpdate.getId()) == true)
		{
			throw new ErrorException("Agent already exist.");
		}
		if(agentUpdateDao.isBkExist(agentUpdate.getEmail()) == true)
		{
			throw new ErrorException("Email already exist!");
		}
		agentUpdateDao.save(agentUpdate);
			return "New agent account successfully created";
	}
	
	public void update(AgentUpdate agentUpdate) throws ErrorException
	{
		if(agentUpdateDao.isIdExist(agentUpdate.getId()) == false)
		{
			throw new ErrorException("Agent not found!");
		}
		if(agentUpdateDao.isBkExist(agentUpdate.getEmail()) == false)
		{
			throw new ErrorException("Agent not found!");
		}
		if(!agentUpdate.getEmail().equals(agentUpdateDao.findById(agentUpdate.getId()).getEmail()))
		{
			throw new ErrorException("Email cannot be changed!");
		}
		agentUpdateDao.save(agentUpdate);
	}
	
	public List<AgentUpdate> findAll ()
	{
		List<AgentUpdate> agents = new ArrayList<AgentUpdate>();
		
		if(agentUpdateDao.findAll().size() > 0)
		{
			agents = agentUpdateDao.findAll();
			
			return agents;
		}
		else
		{
			return agents;
		}
	}
	
	public AgentUpdate findByBk(String username)
	{
		AgentUpdate agent = new AgentUpdate();
		
		if(agentUpdateDao.findByBk(username) != null)
		{
			agent = agentUpdateDao.findByBk(username);
			
			return agent;
		}
		else
		{
			return agent;
		}
	}
	
	public AgentUpdate findById(String id)
	{
		AgentUpdate agent = new AgentUpdate();
		
		if(agentUpdateDao.findById(id) != null)
		{
			return agentUpdateDao.findById(id);
		}
		else
		{
			return agent;
		}
	}
	
	public List<AgentUpdate> findAllWithStatus(String status)
	{
		List<AgentUpdate> agents = new ArrayList<AgentUpdate>();
		
		if(agentUpdateDao.findAllWithStatus(status).size() > 0)
		{
			agents = agentUpdateDao.findAllWithStatus(status);
			
			return agents;
		}
		else
		{
			return agents;
		}
	}
}
