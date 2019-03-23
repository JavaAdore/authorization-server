package com.eltaieb.microservice.rest.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eltaieb.microservice.authorizationserver.facade.AuthorizationServerFacade;
import com.eltaieb.microservice.authorizationserver.model.EntityModel;
import com.eltaieb.microservice.base.exception.ServiceException;
import com.eltaieb.microservice.base.footprint.FootPrint;
import com.eltaieb.microservice.base.model.ServiceResponse;
import com.eltaieb.microservice.base.model.SuccessServiceResponse;

@RestController
@RequestMapping("registration/entity")
public class EntityRegistrationController {

	private AuthorizationServerFacade authorizationServerFacade;
	
	
	public EntityRegistrationController(AuthorizationServerFacade authorizationServerFacade)
	{
		this.authorizationServerFacade=authorizationServerFacade;
	}
	
	
	
	@RequestMapping("/")
	@FootPrint("NEW_ENTITY_REGISTRATION")
	public  ServiceResponse<Long> registerNewEntity(@RequestBody EntityModel entityModel) throws ServiceException
	{
 		Long  entityId =authorizationServerFacade.registerNewEntity(entityModel);
		return new SuccessServiceResponse<Long>(entityId);	 
	}
}
