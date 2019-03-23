package com.eltaieb.microservice.rest.controller;

import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.eltaieb.microservice.authorizationserver.facade.AuthorizationServerFacade;
import com.eltaieb.microservice.authorizationserver.model.ChangePasswordModel;
import com.eltaieb.microservice.authorizationserver.model.ResetPasswordModel;
import com.eltaieb.microservice.base.exception.ServiceException;
import com.eltaieb.microservice.base.footprint.FootPrint;
import com.eltaieb.microservice.base.model.ServiceResponse;
import com.eltaieb.microservice.base.model.SuccessServiceResponse;

@RestController
@RequestMapping("password")
public class PasswordController {
	
	private AuthorizationServerFacade authorizationServerFacade;
	
	public PasswordController(AuthorizationServerFacade authorizationServerFacade)
	{
		this.authorizationServerFacade=authorizationServerFacade;
		
	}
	
	@FootPrint("FORGET_PASSWORD")
	@RequestMapping(path="forget/{username}",method=RequestMethod.GET)
	public  ServiceResponse<String> sendForgetPassword(@PathVariable("username") String username) throws ServiceException
	{
		authorizationServerFacade.sendForgetPassword(username);
		return new SuccessServiceResponse<String>("check "+ username + " you will get ");
	}
	
	

	@FootPrint("CHANGE_PASSWORD")
	@RequestMapping(path="change/{userLoginId}",method=RequestMethod.POST)
	ServiceResponse<String> changePassword( @PathVariable("userLoginId") Long userLoginId, @RequestBody ChangePasswordModel changePasswordModel) throws ServiceException
	{ 
 		authorizationServerFacade.changePassword(userLoginId,changePasswordModel);
		return new SuccessServiceResponse<String>("password changed successfully");
	}
	
	@FootPrint("RESET_PASSWORD_BY_CODE")
	@RequestMapping(path="reset/code" ,method=RequestMethod.POST)
	public  ServiceResponse<OAuth2AccessToken> resetPasswordByCode(@RequestBody ResetPasswordModel resetPasswordModel) throws ServiceException
	{
		 OAuth2AccessToken  oAuth2AccessToken = authorizationServerFacade.resetPasswordByCode(resetPasswordModel);
		 return new SuccessServiceResponse<OAuth2AccessToken>(oAuth2AccessToken);
	}
	
	@FootPrint("RESET_PASSWORD_BY_TOKEN")
	@RequestMapping(path="reset/token" ,method=RequestMethod.POST)
	public  ServiceResponse<OAuth2AccessToken> resetPasswordByToken(@RequestBody ResetPasswordModel resetPasswordModel) throws ServiceException
	{
		 OAuth2AccessToken  oAuth2AccessToken = authorizationServerFacade.resetPasswordByToken(resetPasswordModel);
		 return new SuccessServiceResponse<OAuth2AccessToken>(oAuth2AccessToken);
	}
	  
	 
}
