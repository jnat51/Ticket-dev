package com.spring.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.spring.model.Image;
import com.spring.model.Pic;

@Repository
@Transactional
public class PicDao extends ParentDao {
	public void save(Pic pic) {
		super.entityManager.merge(pic);
	}
	
	public void delete(Pic pic) {
		super.entityManager.remove(pic);
	}
	
	public Pic findById(String id) {
		try {
			String query = "from Pic where id = :id";
			
			Pic pic = (Pic) this.entityManager
					  .createQuery(query)
					  .setParameter("id",id).getSingleResult();
			
			return pic;
			}
			catch(Exception e)
			{
				return null;
			}
	}
}
