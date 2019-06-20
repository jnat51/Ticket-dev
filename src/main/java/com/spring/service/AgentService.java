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
		if(agentDao.isBkExist(agent.getUsername()) == true)
		{
			throw new ErrorException("Username already exist!");
		}
		if(agentDao.isEmailExist(agent.getEmail()) == true) {
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
		if(agentDao.isBkExist(agent.getUsername()) == false)
		{
			throw new ErrorException("Agent not found!");
		}
		if(!agent.getUsername().equals(agentDao.findById(agent.getId()).getUsername()))
		{
			throw new ErrorException("Username cannot be changed!");
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
	
	public Agent findByBk(String username)
	{
		Agent agent = new Agent();
		
		if(agentDao.findByBk(username) != null)
		{
			agent = agentDao.findByBk(username);
			
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
	
	public AgentLogin login(String username) throws ErrorException {
		AgentLogin agent = new AgentLogin();
		
		if(agentDao.login(username) != null)
		{
			agent = agentDao.login(username);
		}
		if(agent.getStatus().equals("active"))
		{
			return agent;
		}
		else
		{
			throw new ErrorException("User is non-active");
		}
	}
	
	public List<Agent> findByFilter(String namaAgent, String username)
	{
		List<Agent> agents = new ArrayList<Agent>();
		
		if(agentDao.findByFilter(namaAgent, username).size() > 0)
		{
			agents = agentDao.findByFilter(namaAgent, username);
			
			return agents;
		}
		else
		{
			return agents;
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
	
	public Agent resetPassword(String email) throws ErrorException
	{
		if(agentDao.findByEmail(email) != null)
		{
			return agentDao.findByEmail(email);
		}
		else {
			return null;
		}
	}
}
