package com.eltaieb.microservice.authorizationserver.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.facebook.api.User;
import org.springframework.social.instagram.api.InstagramProfile;
import org.springframework.stereotype.Service;

import com.eltaieb.microservice.authorizationserver.dao.JpaUserLoginDao;
import com.eltaieb.microservice.authorizationserver.entity.UserLoginEntity;
import com.eltaieb.microservice.authorizationserver.enumuration.AuthenticationChannel;
import com.eltaieb.microservice.authorizationserver.enumuration.VerificationStatus;
import com.eltaieb.microservice.authorizationserver.model.BaseUserInfo;
import com.eltaieb.microservice.authorizationserver.service.api.UserLoginService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;

@Service
public class UserLoginServiceBean implements UserLoginService {

	private static final Logger log = Logger.getLogger(UserLoginServiceBean.class.getName());

	 
	private ModelMapper modelMapper;
	private PasswordEncoder passwordEncoder;
	
	private JpaUserLoginDao jpaUserLoginDao;
	
	public UserLoginServiceBean(JpaUserLoginDao jpaUserLoginDao, PasswordEncoder passwordEncoder)
	{
		this.jpaUserLoginDao=jpaUserLoginDao;
		this.passwordEncoder=passwordEncoder;
	}
 
	@Override
	public UserLoginEntity addOrUpdateUser(Long userId , User facebookUser) {
		UserLoginEntity userLoginEntity = toUserLoginEntity(userId , facebookUser);
		return  jpaUserLoginDao.save(userLoginEntity);
	}
	
	
	@Override
	public Optional<UserLoginEntity> findUserByVerifiedUserName(String userName) {
		return jpaUserLoginDao.findVerifiedUserByUserName(userName);
	}

 
	@Override
	public Optional<UserLoginEntity> findUserByFacebookId(String facebookId) {
		return jpaUserLoginDao.findByFacebookId(facebookId);
 	}
 

	@Override
	public Optional<UserLoginEntity> findUserByGoogleId(String googleId) {
		return jpaUserLoginDao.findByGoogleId(googleId);
	}

 
	@Override
	public UserLoginEntity addOrUpdateUser(Long userId, Payload payload) {
		UserLoginEntity userLoginEntity = toUserLoginEntity(userId , payload);
		return  jpaUserLoginDao.save(userLoginEntity);
	}

	private UserLoginEntity toUserLoginEntity(Long userId, Payload payload) {
		return toUserLoginEntity(userId,payload.getSubject() , "",AuthenticationChannel.GOOGLE, VerificationStatus.THIRD_PARTY);
	}
	
	private UserLoginEntity toUserLoginEntity(Long userId , User facebookUser) {
		return toUserLoginEntity(userId, facebookUser.getId() , "" , AuthenticationChannel.FACEBOOK,VerificationStatus.THIRD_PARTY);
	}

	@Override
	public UserLoginEntity addOrUpdateUser(Long userId, BaseUserInfo baseUserInfo, VerificationStatus verification) {
		String encodedPassword = passwordEncoder.encode(baseUserInfo.getPassword());
		UserLoginEntity userLoginEntity= toUserLoginEntity(userId,baseUserInfo.getUserName(),encodedPassword,AuthenticationChannel.BASIC,verification);
		return jpaUserLoginDao.save(userLoginEntity);
	}
 
	private UserLoginEntity toUserLoginEntity(Long userId, String userName, String password,
			AuthenticationChannel authenticationChannel, VerificationStatus verification) {
		UserLoginEntity userLoginEntity = new UserLoginEntity();
		userLoginEntity.setUserName(userName);
		userLoginEntity.setPassword(password);
		userLoginEntity.setAuthenticationChannel(authenticationChannel);
		userLoginEntity.setUserId(userId);
		userLoginEntity.setVerificationStatus(verification);
		return userLoginEntity;
	}

	@Override
	public Optional<UserLoginEntity> findUserByInstagramId(String instagramId) {
		return jpaUserLoginDao.findByInstagramId(instagramId);
	}

	@Override
	public UserLoginEntity addOrUpdateUser(Long userId, InstagramProfile instagramProfile) {
		UserLoginEntity userLoginEntity = toUserLoginEntity(userId , instagramProfile);
		return  jpaUserLoginDao.save(userLoginEntity);
	}

	private UserLoginEntity toUserLoginEntity(Long userId, InstagramProfile instagramProfile) {
		return toUserLoginEntity(userId,String.valueOf(instagramProfile.getId()), "",AuthenticationChannel.INSTAGRAM,VerificationStatus.THIRD_PARTY);
	}

	@Override
	public UserLoginEntity update(UserLoginEntity userLogin) {
		return jpaUserLoginDao.save(userLogin);		
	}

	@Override
	public List<UserLoginEntity> findUsersByUserName(String userName) {
		return jpaUserLoginDao.findUsersByUserName(userName);
	}

	@Override
	public UserLoginEntity permenantlyBlockFraudCases(UserLoginEntity ul) {
		ul.setPermanentlyLocked(Boolean.TRUE);
		return jpaUserLoginDao.save(ul);
	}

	@Override
	public Optional<UserLoginEntity> findNotVerifiedUserByUserName(String userName) {
 		return jpaUserLoginDao.findNotVerifiedUserByUserName(userName);
	}

	@Override
	public Optional<UserLoginEntity> findUserLogin(Long userLoginId) {
		return jpaUserLoginDao.findById(userLoginId);
	}

	@Override
	public UserLoginEntity save(UserLoginEntity userLoginEntity) {
		return jpaUserLoginDao.save(userLoginEntity);
	}

	@Override
	public Optional<UserLoginEntity> findLogingabledUserByUserName(String userName) {
		return jpaUserLoginDao.findLogingabledUserByUserName(userName);
	}

	 
	




}
