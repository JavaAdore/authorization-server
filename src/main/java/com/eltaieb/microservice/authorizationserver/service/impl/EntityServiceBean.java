package com.eltaieb.microservice.authorizationserver.service.impl;

import org.springframework.stereotype.Service;

import com.eltaieb.microservice.authorizationserver.dao.JpaEntityEntityDao;
import com.eltaieb.microservice.authorizationserver.entity.EntityEntity;
import com.eltaieb.microservice.authorizationserver.service.api.EntityService;

@Service
public class EntityServiceBean implements EntityService{

	private JpaEntityEntityDao jpaEntityEntityDao;
	
	public EntityServiceBean(JpaEntityEntityDao jpaEntityEntityDao)
	{
		this.jpaEntityEntityDao=jpaEntityEntityDao;
	}
	
	
	@Override
	public EntityEntity save(EntityEntity entityEntity) {
 		return jpaEntityEntityDao.save(entityEntity);
	}

	
	
}
