package com.eltaieb.microservice.authorizationserver.dao;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.eltaieb.microservice.authorizationserver.entity.UserLoginEntity;

public interface JpaUserLoginDao extends CrudRepository<UserLoginEntity,Long>{
 
	@Query("select ul from UserLogin ul where ul.userName = :#{#userName} and ul.authenticationChannel = :#{T(com.eltaieb.microservice.authorizationserver.enumuration.AuthenticationChannel).BASIC} and  ul.verificationStatus = :#{T(com.eltaieb.microservice.authorizationserver.enumuration.VerificationStatus).VERIFIED} " )
	Optional<UserLoginEntity> findVerifiedUserByUserName(@Param("userName") String userName);
	
	
	@Query("select ul from UserLogin ul where ul.userName = :#{#userName} and ul.authenticationChannel = :#{T(com.eltaieb.microservice.authorizationserver.enumuration.AuthenticationChannel).BASIC} and  ul.verificationStatus =:#{T(com.eltaieb.microservice.authorizationserver.enumuration.VerificationStatus).NOT_VERIFIED} " )
	Optional<UserLoginEntity> findNotVerifiedUserByUserName(@Param("userName") String userName);

	
	@Query("select ul from UserLogin ul where ul.userName = :#{#facebookId} and ul.authenticationChannel = :#{T(com.eltaieb.microservice.authorizationserver.enumuration.AuthenticationChannel).FACEBOOK}" )
	Optional<UserLoginEntity> findByFacebookId(@Param("facebookId")  String facebookId);

	@Query("select ul from UserLogin ul where ul.userName = :#{#instagramId} and ul.authenticationChannel = :#{T(com.eltaieb.microservice.authorizationserver.enumuration.AuthenticationChannel).INSTAGRAM}" )
	Optional<UserLoginEntity> findByInstagramId(@Param("instagramId")  String instagramId);

	
	@Query("select ul from UserLogin ul where ul.userName = :#{#googleId} and ul.authenticationChannel = :#{T(com.eltaieb.microservice.authorizationserver.enumuration.AuthenticationChannel).GOOGLE}" )
	Optional<UserLoginEntity> findByGoogleId(@Param("googleId") String googleId);

	// used only when multiple user pretend ownership of email before verification ( fraud case)
	@Query("select ul from UserLogin ul where ul.userName = :#{#userName} and ul.authenticationChannel = :#{T(com.eltaieb.microservice.authorizationserver.enumuration.AuthenticationChannel).BASIC}" )
	List<UserLoginEntity> findUsersByUserName(@Param("userName") String userName);

	@Query("select ul from UserLogin ul where ul.userName = :#{#userName} and ul.authenticationChannel = :#{T(com.eltaieb.microservice.authorizationserver.enumuration.AuthenticationChannel).BASIC} and  ul.verificationStatus in (  :#{T(com.eltaieb.microservice.authorizationserver.enumuration.VerificationStatus).VERIFIED} ,  :#{T(com.eltaieb.microservice.authorizationserver.enumuration.VerificationStatus).NOT_VERIFIED} )" )
	Optional<UserLoginEntity> findLogingabledUserByUserName(@Param("userName") String username);

	@Query("select ul from UserLogin ul where ul.userName = :#{#userName} and ul.password =:#{#password}")
	List<UserLoginEntity> findUserLoginEntities(@Param("userName") String userName, @Param("password") String password);


 
}
