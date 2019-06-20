package com.spring.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.spring.model.agent.Agent;
import com.spring.model.agent.AgentLogin;
import com.spring.model.agent.AgentWithImage;

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
	
	public AgentWithImage findWithImage(String id)
	{
		try {
			String query = "SELECT tbl_agent.id, tbl_agent.username, tbl_agent.password, tbl_agent.name, tbl_agent.email, tbl_agent.status, tbl_image.image FROM tbl_agent " + 
					"LEFT JOIN tbl_image ON tbl_agent.image_id = tbl_image.id " + 
					"WHERE tbl_agent.id = :id";
			
			AgentWithImage agent = (AgentWithImage) super.entityManager
					  .createNativeQuery(query)
					  .setParameter("id",id).getSingleResult();
			
			return agent;
			}
			catch(Exception e)
			{
				return null;
			}
	}
	
	public Object getMaxPage() {
		try {
			String query = "SELECT COUNT(*) FROM tbl_agent";
			
			Object row = (Object) super.entityManager
					  .createNativeQuery(query).getSingleResult();
			
			System.out.println("test");
			
			return row;			
		} catch(Exception e) {
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
	
	public boolean isEmailExist(String email)
	{
		if(findByEmail(email) == null)
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
	
	public boolean passwordVerification(String password) {
		try {
			String query = "from Agent where password = :password";
			
			Agent agent = (Agent) super.entityManager.createQuery(query).setParameter("password", password)
					.getSingleResult();
			
			if (agent.getId() == null) {
				return true;
			} else {
				return false;
			}
		}catch (Exception e) {
			System.out.println("Wrong password.");

			return false;
		}
	}
	
	public Agent findByEmail(String email) {
		try {
			String query = "from Agent WHERE email = :email";
			
			Agent agent = (Agent) super.entityManager.createQuery(query).setParameter("email", email)
					.getSingleResult();
			
			return agent;
		}
		catch (Exception e) {
			return null;
		}
	}
	
	public AgentLogin login(String username) {
		try {
			System.out.println("login");
			String query = "SELECT tbl_agent.id, tbl_agent.username, tbl_agent.password, tbl_agent.name, tbl_agent.email, tbl_agent.status, tbl_image.image FROM tbl_agent " + 
					"LEFT JOIN tbl_image ON tbl_agent.image_id = tbl_image.id " + 
					"WHERE tbl_user.username = :username";

			AgentLogin agent = (AgentLogin) super.entityManager.createNativeQuery(query, AgentLogin.class).setParameter("username", username)
					.getSingleResult();

			return agent;
		} catch (Exception e) {
			return null;
		}
	}
	
	public List<Agent> findAll (){
		try {
			String query = "from Agent";
			
			List<Agent> agents = new ArrayList<Agent>();
			
			agents = super.entityManager.createQuery(query).getResultList();

			return agents;
		}
		catch (Exception e) {
			return null;
		}
	}
	
	public List<Agent> findAllWithStatus(String status){
		try {
			String query = "SELECT * FROM tbl_agent WHERE status = :status";
			
			List<Agent> agents = new ArrayList<Agent>();
			
			agents = super.entityManager.createNativeQuery(query, Agent.class)
					.setParameter("status", status)
					.getResultList();

			return agents;
		} catch(Exception e) {
			return null;
		}
	}
}
