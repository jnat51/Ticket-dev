package com.spring.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.spring.model.agent.Agent;
import com.spring.model.agent.AgentUpdate;

@Repository
public class AgentUpdateDao extends ParentDao{
	public void save(AgentUpdate agentUpdate) {
		System.out.println("merge agent");
		super.entityManager.merge(agentUpdate);
		System.out.println("merge success");
	}
	
	public void delete(AgentUpdate agentUpdate) {
		System.out.println("remove agent");
		super.entityManager.remove(agentUpdate);
		System.out.println("remove success");
	}
	
	public AgentUpdate findById(String id)
	{
		try {
			System.out.println("find agent by id");
			String query = "from AgentUpdate where id = :id";
			
			AgentUpdate agentUpdate = (AgentUpdate) super.entityManager
					  .createQuery(query)
					  .setParameter("id",id).getSingleResult();
			
			return agentUpdate;
			}
			catch(Exception e)
			{
				return null;
			}
	}
	
	public AgentUpdate findByBk(String email)
	{
		try {
			System.out.println("find agent by bk");
			String query = "from AgentUpdate where email = :email";
			
			AgentUpdate agentUpdate = (AgentUpdate) super.entityManager
					  .createQuery(query)
					  .setParameter("email", email)
					  .getSingleResult();
			
			return agentUpdate;
			}
			catch(Exception e)
			{
				return null;
			}
	}
	
	public boolean isIdExist(String id)
	{
		if(findById(id) == null)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	public boolean isBkExist(String username)
	{
		if(findByBk(username) == null)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	public List<AgentUpdate> findAllWithStatus(String status){
		try {
			String query = "SELECT * FROM update_agent WHERE status = :status";
			
			List<AgentUpdate> agents = new ArrayList<AgentUpdate>();
			
			agents = super.entityManager.createNativeQuery(query, Agent.class)
					.setParameter("status", status)
					.getResultList();

			return agents;
		} catch(Exception e) {
			return null;
		}
	}
	
	public List<AgentUpdate> findAll (){
		try {
			String query = "from AgentUpdate";
			
			List<AgentUpdate> agents = new ArrayList<AgentUpdate>();
			
			agents = super.entityManager.createQuery(query).getResultList();

			return agents;
		}
		catch (Exception e) {
			return null;
		}
	}
}
