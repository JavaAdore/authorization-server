package com.eltaieb.microservice.authorizationserver.service.api;

import java.util.List;

import com.eltaieb.microservice.authorizationserver.entity.UserVerificationEntity;
import com.eltaieb.microservice.authorizationserver.enumuration.VerificationStatus;
import com.eltaieb.microservice.authorizationserver.model.BaseUserInfo;
import com.eltaieb.microservice.authorizationserver.model.ChangePasswordModel;
import com.eltaieb.microservice.authorizationserver.model.EntityModel;
import com.eltaieb.microservice.authorizationserver.model.FacebookLoginModel;
import com.eltaieb.microservice.authorizationserver.model.GoogleLoginModel;
import com.eltaieb.microservice.authorizationserver.model.UserNamePasswordLoginModel;
import com.eltaieb.microservice.base.exception.ServiceException;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;

public interface ValidationService {

	void validateRegisterNewPublicUser(BaseUserInfo baseUserInfo) throws ServiceException;

	void validate(FacebookLoginModel facebookLoginModel) throws ServiceException;

	void validate(GoogleLoginModel googleLoginModel) throws ServiceException;

	void validate(UserNamePasswordLoginModel userNamePasswordLoginModel) throws ServiceException;

	void validate(GoogleIdToken googleIdToken)throws ServiceException;

	void validateTokenExpiry(UserVerificationEntity userVerificationEntity)throws ServiceException;

	void validateLastTokenSentDate(UserVerificationEntity userVerificationEntity) throws ServiceException;

	boolean isVerified(VerificationStatus verificationStatus);

	void validateUserPassword(String password) throws ServiceException;

	void validateChangePassword(Long userLoginId, ChangePasswordModel changePasswordModel) throws ServiceException;

	void validateRequestVerificationToken(String username) throws ServiceException;

	void validateRegisterNewSystemUser(String userName, Long entityId, List<String> roles) throws  ServiceException;

	void validateRegisterNewEntity(EntityModel entityModel);

}
