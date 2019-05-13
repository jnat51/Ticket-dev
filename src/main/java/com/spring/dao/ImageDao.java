package com.spring.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.spring.model.Image;

@Repository
@Transactional
public class ImageDao extends ParentDao{
	public void saveImage(Image image) {
		super.entityManager.merge(image);
	}
	
	public void deleteImage(Image image) {
		super.entityManager.remove(image);
	}
	
	public Image findById(String id) {
		try {
			String query = "from Image where id = :id";
			
			Image image = (Image) this.entityManager
					  .createQuery(query)
					  .setParameter("id",id).getSingleResult();
			
			return image;
			}
			catch(Exception e)
			{
				return null;
			}
	}
	
	public Image findByBk(String fileName, byte[] data)
	{
		try {
			System.out.println("find image by bk");
			String query = "from Image where fileName = :filename AND image = :data";
			
			Image image = (Image) this.entityManager
					  .createQuery(query)
					  .setParameter("filename", fileName)
					  .setParameter("data", data)
					  .getSingleResult();
			
			return image;
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
	
	public boolean isBkExist(String fileName, byte[] data)
	{
		if(findByBk(fileName, data) == null)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
}
