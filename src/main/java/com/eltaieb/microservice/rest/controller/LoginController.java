package com.eltaieb.microservice.rest.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.social.facebook.api.User;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.social.instagram.api.InstagramProfile;
import org.springframework.social.instagram.api.impl.InstagramTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eltaieb.microservice.authorizationserver.config.ServiceConstant;
import com.eltaieb.microservice.authorizationserver.facade.AuthorizationServerFacade;
import com.eltaieb.microservice.authorizationserver.model.FacebookLoginModel;
import com.eltaieb.microservice.authorizationserver.model.GoogleLoginModel;
import com.eltaieb.microservice.authorizationserver.model.InstagramLoginModel;
import com.eltaieb.microservice.authorizationserver.model.UserNamePasswordLoginModel;
import com.eltaieb.microservice.authorizationserver.service.impl.AccessTokenGeneratorBean;
import com.eltaieb.microservice.authorizationserver.service.model.CustomUserDetails;
import com.eltaieb.microservice.base.exception.ServiceException;
import com.eltaieb.microservice.base.footprint.FootPrint;
import com.eltaieb.microservice.base.model.ServiceResponse;
import com.eltaieb.microservice.base.model.SuccessServiceResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

 
@RestController
@RequestMapping("login/")
public class LoginController {
	
	private static final Logger log = Logger.getLogger(LoginController.class.getName());
	
	
	private final static String[] FACEBOOK_BASIC_INFO_FIELDS = { "id", "email", "first_name", "last_name", "birthday",
			"picture", "gender" };

	@Autowired
	ServiceConstant serviceConstant;

	 
	@Autowired
	AccessTokenGeneratorBean accessTokenGenerator;
	
	@Autowired
	private AuthorizationServerFacade authorizationServerFacade;
	
	
	@Autowired
	private AuthenticationManager authenticationManager;


	@PostMapping("/facebook")
	@FootPrint("FACEBOOK_LOGIN")
	public ServiceResponse<OAuth2AccessToken> facebookLogin(@RequestBody FacebookLoginModel facebookLoginModel) throws ServiceException {
		
		authorizationServerFacade.validate(facebookLoginModel);
 		
		FacebookTemplate facebook = new FacebookTemplate(facebookLoginModel.getFacebookAccessToken());
		 User facebookUser = facebook.fetchObject("me",
				org.springframework.social.facebook.api.User.class, FACEBOOK_BASIC_INFO_FIELDS);
		
		 OAuth2AccessToken oAuth2AccessToken = authorizationServerFacade.facebookLogin(facebookUser);
		
		 return new SuccessServiceResponse<OAuth2AccessToken>(oAuth2AccessToken);
		
	}

	@PostMapping("/google")
	@FootPrint("GOOGLE_LOGIN")
	public ServiceResponse googleLogin(@RequestBody GoogleLoginModel googleLoginModel)
			throws GeneralSecurityException, IOException, ServiceException {
		
		authorizationServerFacade.validate(googleLoginModel);
		
		GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(),
				JacksonFactory.getDefaultInstance())
						.setAudience(Collections.singletonList(serviceConstant.getGoogleClientId())).build();
		GoogleIdToken idToken = verifier.verify(googleLoginModel.getGoogleClientId());
		
		 OAuth2AccessToken oAuth2AccessToken = authorizationServerFacade.googleLogin(idToken);
		
		 return new SuccessServiceResponse<OAuth2AccessToken>(oAuth2AccessToken);

		
	}
	
	@PostMapping("/basic")
	@FootPrint("BASIC_LOGIN")
	public ServiceResponse<OAuth2AccessToken> login(@RequestBody UserNamePasswordLoginModel userNamePasswordLoginModel) throws ServiceException
	{
		
			authorizationServerFacade.validate(userNamePasswordLoginModel);
			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userNamePasswordLoginModel.getUserName(), userNamePasswordLoginModel.getPassword());
			Authentication  authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
			CustomUserDetails user = (CustomUserDetails)authentication.getPrincipal();
			OAuth2AccessToken oAuth2AccessToken =  accessTokenGenerator.generateOAuth2AccessToken(user);
 
		 return new SuccessServiceResponse<OAuth2AccessToken>(oAuth2AccessToken);

	}
	
	
	
	@PostMapping("/instagram")
	@FootPrint("INSTAGRAM_LOGIN")
	public ServiceResponse<OAuth2AccessToken> instagramLogin(@RequestBody InstagramLoginModel instagramLoginModel) throws ServiceException {
		
		InstagramTemplate instagramTemplate = new InstagramTemplate(null, instagramLoginModel.getInstagramAccessToken());
		
		InstagramProfile instagramProfile = instagramTemplate.userOperations().getUser();
		
 		 OAuth2AccessToken oAuth2AccessToken = authorizationServerFacade.instagramLogin(instagramProfile);
 		
 		 return new SuccessServiceResponse<OAuth2AccessToken>(oAuth2AccessToken);
	}
	
 

	
	
	
	 
}