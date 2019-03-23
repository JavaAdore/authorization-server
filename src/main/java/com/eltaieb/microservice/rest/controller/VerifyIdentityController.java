package com.eltaieb.microservice.rest.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.eltaieb.microservice.authorizationserver.facade.AuthorizationServerFacade;
import com.eltaieb.microservice.base.exception.ServiceException;
import com.eltaieb.microservice.base.footprint.FootPrint;
import com.eltaieb.microservice.base.model.ServiceResponse;
import com.eltaieb.microservice.base.model.SuccessServiceResponse;

@RestController
@RequestMapping("verification")
public class VerifyIdentityController {
	
	private AuthorizationServerFacade authorizationServerFacade;
	
	public VerifyIdentityController(AuthorizationServerFacade authorizationServerFacade)
	{
		this.authorizationServerFacade=authorizationServerFacade;
		
	}
	
	@FootPrint("VALIDATE_IDENTITY_BY_TOKEN")
	@RequestMapping("username/ownership/token/{token}")
	public  ServiceResponse<String> verifyByToken(@PathVariable("token") String uriEncodedToken) throws ServiceException
	{
		authorizationServerFacade.verifyUserNameOwnershipByToken(uriEncodedToken);
		return new SuccessServiceResponse<String>("user has been verified successfully .. thank you");
	}
	
	@FootPrint("VALIDATE_IDENTITY_BY_CODE")
	@RequestMapping("username/ownership/code/{code}")
	public  ServiceResponse<String> verifyByCodeToken(@PathVariable("code") String code) throws ServiceException
	{
		authorizationServerFacade.verifyUserNameOwnershipByCode(code);
		return new SuccessServiceResponse<String>("user has been verified successfully .. thank you");
	}
	
	
	@FootPrint("REQUEST_VERIFICATION_TOKEN")
	@RequestMapping(path="username/ownership/request-token/{username}",method=RequestMethod.GET)
	public  ServiceResponse<String> requestVerificationToken(@PathVariable("username") String username) throws ServiceException
	{
		authorizationServerFacade.requestVerificationToken(username);
		return new SuccessServiceResponse<String>("check "+ username + " you will get ");
	}
	
	
	
	
}
