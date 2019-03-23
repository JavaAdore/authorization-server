package com.eltaieb.microservice.authorizationserver.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.eltaieb.microservice.authorizationserver.config.ServiceConstant;
import com.eltaieb.microservice.authorizationserver.dao.JpaUserVerificationDao;
import com.eltaieb.microservice.authorizationserver.entity.UserLoginEntity;
import com.eltaieb.microservice.authorizationserver.entity.UserVerificationEntity;
import com.eltaieb.microservice.authorizationserver.enumuration.VerificationChannel;
import com.eltaieb.microservice.authorizationserver.enumuration.VerificationReason;
import com.eltaieb.microservice.authorizationserver.service.api.UserIdentityVerificationService;

@Service
public class UserIdentityVerificationServiceBean implements UserIdentityVerificationService {

	public JpaUserVerificationDao jpaUserVerificationDao;

	public UserIdentityVerificationServiceBean(JpaUserVerificationDao jpaUserVerificationDao) {
		this.jpaUserVerificationDao = jpaUserVerificationDao;
	}

	@Override
	public Optional<UserVerificationEntity> findUserVerificationEntityByTokenAndReason(String decodedToken,VerificationReason reason) {
		return jpaUserVerificationDao.findByTokenAndReason(decodedToken,reason);
	}

	@Override
	public UserVerificationEntity generateUserVerificationEntity(UserLoginEntity userLogin,VerificationReason reason) {
		UserVerificationEntity userVerificationEntity = new UserVerificationEntity();
		userVerificationEntity.setToken(generateRandomEncodedToken());
		userVerificationEntity.setCode(generateRandomCode());
		userVerificationEntity.setUserLogin(userLogin);
		userVerificationEntity.setLastTokenSentDate(LocalDateTime.now());
		userVerificationEntity.setTokenExpiryDate(LocalDateTime.now().plus(
				ServiceConstant.VERIFICATION_EMAIL_EXPIRY_AMOUNT, ServiceConstant.VERIFICATION_EMAIL_EXPIRY_UNIT));
		userVerificationEntity.setVerificationChannel(getApproperateVerificationChannel(userLogin.getUserName()));
		userVerificationEntity.setReason(reason);
		return jpaUserVerificationDao.save(userVerificationEntity);
	}

	private String generateRandomCode() {
		StringBuilder verificationCodeStringBuilder = new StringBuilder();
		Random random = new Random();
		Stream.iterate(1, (i) -> i + 1).limit(ServiceConstant.MAX_USER_VERIFICATION_CODE_LENGTH).forEach(r -> {
			verificationCodeStringBuilder.append(random.nextInt(9));
		});
		return verificationCodeStringBuilder.toString();
	}

	private VerificationChannel getApproperateVerificationChannel(String userName) {
		if (userName.indexOf("@") != -1) {
			return VerificationChannel.EMAIL;
		}
		return VerificationChannel.SMS;
	}

	private String generateRandomEncodedToken() {
		String tokenString = UUID.randomUUID().toString() + UUID.randomUUID().toString();
		try {
			return URLEncoder.encode(tokenString, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// UTF-8 already supported as per the documentation
			// so it has no sense to face this exception anyway
			return null;
		}
	}

	@Override
	public void update(UserVerificationEntity userVerificationEntity) {
		jpaUserVerificationDao.save(userVerificationEntity);
	}

	@Override
	public Optional<UserVerificationEntity> findUserVerificationEntityByCodeAndReason(String verificationCode,VerificationReason reason) {
		return jpaUserVerificationDao.findByCodeAndReason(verificationCode,reason);
	}

}
