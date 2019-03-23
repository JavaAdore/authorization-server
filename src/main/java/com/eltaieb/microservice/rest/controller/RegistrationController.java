package com.eltaieb.microservice.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eltaieb.microservice.authorizationserver.facade.AuthorizationServerFacade;
import com.eltaieb.microservice.authorizationserver.model.BaseUserInfo;
import com.eltaieb.microservice.authorizationserver.service.impl.AccessTokenGeneratorBean;
import com.eltaieb.microservice.base.exception.ServiceException;
import com.eltaieb.microservice.base.footprint.FootPrint;
import com.eltaieb.microservice.base.model.ServiceResponse;
import com.eltaieb.microservice.base.model.SuccessServiceResponse;

@RestController
@RequestMapping("registration/customer")
public class RegistrationController {

 
	
	@Autowired
	private AuthorizationServerFacade authorizationServerFacade;

	@Autowired
	AccessTokenGeneratorBean AccessTokenGenerator;
	
	@RequestMapping("/")
	@FootPrint("NEW_USER_REGISTRATION")
	public  ServiceResponse<OAuth2AccessToken> registerNewPublicUser(@RequestBody BaseUserInfo baseUserInfo) throws ServiceException
	{
 		OAuth2AccessToken  oAuth2AccessToken =authorizationServerFacade.registerNewPublicUser(baseUserInfo);
		return new SuccessServiceResponse<OAuth2AccessToken>(oAuth2AccessToken);	 
	}
	
	
	
	
	

}