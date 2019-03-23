package com.eltaieb.microservice.authorizationserver.dao;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.eltaieb.microservice.authorizationserver.entity.UserVerificationEntity;
import com.eltaieb.microservice.authorizationserver.enumuration.VerificationReason;

public interface JpaUserVerificationDao extends CrudRepository<UserVerificationEntity,Long>{
 
 	Optional<UserVerificationEntity> findByTokenAndReason(String token,VerificationReason reason);

	Optional<UserVerificationEntity> findByCodeAndReason(String verificationCode,VerificationReason reason);
	
	 

	 
}
