package com.eltaieb.microservice.authorizationserver.service.api;

import java.util.Optional;

import com.eltaieb.microservice.authorizationserver.entity.UserLoginEntity;
import com.eltaieb.microservice.authorizationserver.entity.UserVerificationEntity;
import com.eltaieb.microservice.authorizationserver.enumuration.VerificationReason;


public interface UserIdentityVerificationService {

 
	Optional<UserVerificationEntity> findUserVerificationEntityByTokenAndReason(String decodedToken,VerificationReason reason);

	Optional<UserVerificationEntity> findUserVerificationEntityByCodeAndReason(String verificationCode,VerificationReason reason);

	
	UserVerificationEntity generateUserVerificationEntity(UserLoginEntity userLogin,VerificationReason reason);

	void update(UserVerificationEntity userVerificationEntity);

 
 

	
 	
	
}
