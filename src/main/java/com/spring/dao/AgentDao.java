package com.spring.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.spring.model.Agent;

@Repository
@Transactional
public class AgentDao extends ParentDao{	
	public void save(Agent agent) {
		System.out.println("merge agent");
		super.entityManager.merge(agent);
		System.out.println("merge success");
	}
	
	public void delete(Agent agent) {
		System.out.println("remove agent");
		super.entityManager.remove(agent);
		System.out.println("remove success");
	}
	
	public Agent findById(String id)
	{
		try {
			System.out.println("find agent by id");
			String query = "from Agent where id = :id";
			
			Agent agent = (Agent) super.entityManager
					  .createQuery(query)
					  .setParameter("id",id).getSingleResult();
			
			return agent;
			}
			catch(Exception e)
			{
				return null;
			}
	}
	
	public Agent findByBk(String username)
	{
		try {
			System.out.println("find agent by bk");
			String query = "from Agent where username = :username";
			
			Agent agent = (Agent) super.entityManager
					  .createQuery(query)
					  .setParameter("username", username)
					  .getSingleResult();
			
			return agent;
			}
			catch(Exception e)
			{
				return null;
			}
	}
	
	public boolean login(String username, String password)
	{
		try {
			String query = "from Agent where username = :username";
			
			Agent agent = (Agent) super.entityManager
					  .createQuery(query)
					  .setParameter("username",username)
					  .getSingleResult();
			
			System.out.println("Welcome " + agent.getName());
			
			return true;
			}
			catch(Exception e)
			{
				System.out.println("Wrong username/password.");
				
				return false;
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
		
		Query query = super.entityManager
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
}
