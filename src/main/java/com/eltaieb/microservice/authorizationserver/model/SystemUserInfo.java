package com.eltaieb.microservice.authorizationserver.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SystemUserInfo extends BaseUserInfo{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private List<String> roles;

}
