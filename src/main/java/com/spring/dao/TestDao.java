package com.spring.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.spring.model.Test;

@Repository
@Transactional
public class TestDao extends ParentDao{
	public void insert(Test test)
	{
		super.entityManager.merge(test);
	}
}
