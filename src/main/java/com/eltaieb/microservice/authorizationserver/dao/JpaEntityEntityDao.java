package com.eltaieb.microservice.authorizationserver.dao;
import org.springframework.data.repository.CrudRepository;

import com.eltaieb.microservice.authorizationserver.entity.EntityEntity;

public interface JpaEntityEntityDao extends CrudRepository<EntityEntity,Long>{
  
	
	
}
