package com.eltaieb.microservice.authorizationserver.facade;

import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.social.facebook.api.User;
import org.springframework.social.instagram.api.InstagramProfile;

import com.eltaieb.microservice.authorizationserver.entity.UserLoginEntity;
import com.eltaieb.microservice.authorizationserver.entity.UserVerificationEntity;
import com.eltaieb.microservice.authorizationserver.enumuration.VerificationReason;
import com.eltaieb.microservice.authorizationserver.model.BaseUserInfo;
import com.eltaieb.microservice.authorizationserver.model.ChangePasswordModel;
import com.eltaieb.microservice.authorizationserver.model.EntityModel;
import com.eltaieb.microservice.authorizationserver.model.FacebookLoginModel;
import com.eltaieb.microservice.authorizationserver.model.GoogleLoginModel;
import com.eltaieb.microservice.authorizationserver.model.ResetPasswordModel;
import com.eltaieb.microservice.authorizationserver.model.SystemUserRegistrationInput;
import com.eltaieb.microservice.authorizationserver.model.UserNamePasswordLoginModel;
import com.eltaieb.microservice.base.exception.ServiceException;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;


public interface AuthorizationServerFacade {

	OAuth2AccessToken facebookLogin(User facebookUser) throws ServiceException;

	OAuth2AccessToken googleLogin(GoogleIdToken idToken) throws ServiceException;

	OAuth2AccessToken instagramLogin(InstagramProfile instagramProfile) throws ServiceException;
	
	OAuth2AccessToken registerNewPublicUser(BaseUserInfo baseUserInfo) throws ServiceException;

	void validate(FacebookLoginModel facebookLoginModel) throws ServiceException;

	void validate(GoogleLoginModel googleLoginModel) throws ServiceException;

	void validate(UserNamePasswordLoginModel userNamePasswordLoginModel) throws ServiceException;

	void verifyUserNameOwnershipByToken(String uriEncodedToken)throws ServiceException;

	void verifyUserNameOwnershipByCode(String code) throws ServiceException;

	void sendForgetPassword(String username) throws ServiceException;

	OAuth2AccessToken resetPasswordByCode(ResetPasswordModel resetPasswordModel) throws ServiceException;

	OAuth2AccessToken resetPasswordByToken(ResetPasswordModel resetPasswordModel) throws ServiceException;

	UserVerificationEntity sendVerificationToken(UserLoginEntity userLogin,
			VerificationReason reason);

	void changePassword(Long userLoginId , ChangePasswordModel changePasswordModel) throws ServiceException;

	void requestVerificationToken(String username)throws ServiceException;


	void registerNewSystemUser(SystemUserRegistrationInput systemUserRegistrationInput, Long entityId) throws ServiceException;

	Long registerNewEntity(EntityModel entityModel) throws ServiceException;
	


}
