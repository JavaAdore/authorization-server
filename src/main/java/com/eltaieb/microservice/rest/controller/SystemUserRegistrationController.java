package com.eltaieb.microservice.rest.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eltaieb.microservice.authorizationserver.facade.AuthorizationServerFacade;
import com.eltaieb.microservice.authorizationserver.model.SystemUserRegistrationInput;
import com.eltaieb.microservice.base.exception.ServiceException;
import com.eltaieb.microservice.base.footprint.FootPrint;
import com.eltaieb.microservice.base.model.ServiceResponse;
import com.eltaieb.microservice.base.model.SuccessServiceResponse;

@RestController
@RequestMapping("registration/system-user")
public class SystemUserRegistrationController {

	
	
	private AuthorizationServerFacade authorizationServerFacade;

	
	public SystemUserRegistrationController(AuthorizationServerFacade authorizationServerFacade)
	{
		this.authorizationServerFacade=authorizationServerFacade;
	}
	
	
	
	@RequestMapping(path="/{refrencedSystemEntityId}")
	@FootPrint("NEW_SYSTEM_REGISTRATION")
	public  ServiceResponse<Object> registerNewSystemUser( @PathVariable("refrencedSystemEntityId") Long refrencedSystemEntityId,  @RequestBody SystemUserRegistrationInput systemUserRegistrationInput ) throws ServiceException
	{
 		   authorizationServerFacade.registerNewSystemUser( systemUserRegistrationInput , refrencedSystemEntityId);
 		   return new SuccessServiceResponse<Object>(null);
	}
}
