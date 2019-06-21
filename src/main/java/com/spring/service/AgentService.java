package com.spring.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.dao.AgentDao;
import com.spring.exception.ErrorException;
import com.spring.model.agent.Agent;
import com.spring.model.agent.AgentLogin;

@Service
public class AgentService {
	@Autowired
	AgentDao agentDao;
	
	public void delete(String agentId) throws ErrorException
	{
		if(agentDao.isIdExist(agentId) == true)
		{
			agentDao.delete(agentDao.findById(agentId));
		}
		else
		{
			throw new ErrorException("Agent not found!");
		}
	}
	
	public String insert(Agent agent) throws ErrorException
	{
		if(agentDao.isIdExist(agent.getId()) == true)
		{
			throw new ErrorException("Agent already exist.");
		}
		if(agentDao.isBkExist(agent.getEmail()) == true)
		{
			throw new ErrorException("Email already exist!");
		}
			agentDao.save(agent);
			return "New agent account successfully created";
	}
	
	public void update(Agent agent) throws ErrorException
	{
		if(agentDao.isIdExist(agent.getId()) == false)
		{
			throw new ErrorException("Agent not found!");
		}
		if(agentDao.isBkExist(agent.getEmail()) == false)
		{
			throw new ErrorException("Agent not found!");
		}
		if(!agent.getEmail().equals(agentDao.findById(agent.getId()).getEmail()))
		{
			throw new ErrorException("Email cannot be changed!");
		}
		agentDao.save(agent);
	}
	
	public List<Agent> findAll ()
	{
		List<Agent> agents = new ArrayList<Agent>();
		
		if(agentDao.findAll().size() > 0)
		{
			agents = agentDao.findAll();
			
			return agents;
		}
		else
		{
			return agents;
		}
	}
	
	public int getMaxPage() {
		System.out.println(agentDao.getMaxPage());
		if(((BigInteger) agentDao.getMaxPage()).intValue() > 0)
		{
			return ((BigInteger)agentDao.getMaxPage()).intValue();
		}
		else
		{
			return 0;
		}
	}
	
	public Agent findByBk(String email)
	{
		Agent agent = new Agent();
		
		if(agentDao.findByBk(email) != null)
		{
			agent = agentDao.findByBk(email);
			
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
	
	public List<Agent> findAllWithStatus(String status)
	{
		List<Agent> agents = new ArrayList<Agent>();
		
		if(agentDao.findAllWithStatus(status).size() > 0)
		{
			agents = agentDao.findAllWithStatus(status);
			
			return agents;
		}
		else
		{
			return agents;
		}
	}
	
	public boolean passwordVerification(String password) throws ErrorException
	{
		if(agentDao.passwordVerification(password) == false) {
			throw new ErrorException("Password is not match.");
		}
		else {
			return true;
		}
	}
}
