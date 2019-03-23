package com.eltaieb.microservice.authorizationserver.service.api;

import java.util.List;
import java.util.Optional;

import org.springframework.social.facebook.api.User;
import org.springframework.social.instagram.api.InstagramProfile;

import com.eltaieb.microservice.authorizationserver.entity.UserLoginEntity;
import com.eltaieb.microservice.authorizationserver.enumuration.VerificationStatus;
import com.eltaieb.microservice.authorizationserver.model.BaseUserInfo;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;


public interface UserLoginService {

 

	Optional<UserLoginEntity> findUserByVerifiedUserName(String userName);
	// used only when two users pretend ownership of save email before verification (fraud case )
	List<UserLoginEntity> findUsersByUserName(String userName);

	//
	Optional<UserLoginEntity> findNotVerifiedUserByUserName(String userName);

	
	Optional<UserLoginEntity> findUserByFacebookId(String facebookId);

	Optional<UserLoginEntity> findUserByInstagramId(String instagramId);

	Optional<UserLoginEntity> findUserByGoogleId(String googleId);

	
	UserLoginEntity addOrUpdateUser(Long userId, User user);

	UserLoginEntity addOrUpdateUser(Long userId, Payload payload);

	UserLoginEntity addOrUpdateUser(Long userId, BaseUserInfo baseUserInfo, VerificationStatus verification);

	UserLoginEntity addOrUpdateUser(Long userId, InstagramProfile instagramProfile);

	UserLoginEntity update(UserLoginEntity userLogin);
	
	UserLoginEntity permenantlyBlockFraudCases(UserLoginEntity ul);
	
	Optional<UserLoginEntity> findUserLogin(Long userLoginId);
	
	UserLoginEntity save(UserLoginEntity userLoginEntity);
	
	Optional<UserLoginEntity> findLogingabledUserByUserName(String userName);
	
 
 	
	
}
