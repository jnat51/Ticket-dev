package com.spring.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.spring.model.Admin;

public class AdminDao extends ParentDao{
	EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("TestPersistence");
	EntityManager entityManager = emFactory.createEntityManager();
	
	public void insert(Admin admin) {
		System.out.println("insert agent");
		super.entityManager.merge(admin);
		System.out.println("insert success");
	}
	
	public void update(Admin admin) {
		this.entityManager.merge(admin);
	}
	
	public void delete(Admin admin) {
		this.entityManager.remove(admin);
	}
	
}
