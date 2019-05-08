package com.spring.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.spring.model.Agent;

@Repository
@Transactional
public class AgentDao extends ParentDao{
	public void insert(Agent agent) {
		this.entityManager.merge(agent);
	}
	
	public void update(Agent agent) {
		this.entityManager.merge(agent);
	}
	
	public void delete(Agent agent) {
		this.entityManager.remove(agent);
	}
	
	public Agent findById(String id)
	{
		try {
			String query = "from Agent where id = :id";
			
			Agent agent = (Agent) this.entityManager
					  .createQuery(query)
					  .setParameter("id",id).getSingleResult();
			
			return agent;
			}
			catch(Exception e)
			{
				return new Agent();
			}
	}
	
	public Agent findByBk(String email)
	{
		try {
			String query = "from Agent where email = :email";
			
			Agent agent = (Agent) this.entityManager
					  .createQuery(query)
					  .setParameter("email",email).getSingleResult();
			
			return agent;
			}
			catch(Exception e)
			{
				return new Agent();
			}
	}
	
	@SuppressWarnings("unchecked")
	public List<Agent> findByFilter(String nama,String username)
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append("FROM Barang WHERE 1=1");
		
		if (!nama.trim().isEmpty())
		{
			sb.append(" AND nama LIKE :nama");
		}
		if(!username.trim().isEmpty())
		{
			sb.append(" AND username LIKE :username");
		}
		
		Query query = this.entityManager
		  .createQuery(sb.toString());
		if (!nama.trim().isEmpty())
	  	{
		  	query.setParameter("nama", nama);
		}
		if(!username.trim().isEmpty())
		{
		  	query.setParameter("username", username);
		}
			
		  List<Agent> agents = query.getResultList();
		
		return agents;
	}
	
	public boolean isIdExist(String id)
	{
		if(findByBk(id) == null)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	public boolean isBkExist(String email)
	{
		if(findByBk(email) == null)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
}
