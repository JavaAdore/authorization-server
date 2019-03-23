package com.eltaieb.microservice.authorizationserver.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class GoogleLoginModel implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String googleClientId;
	
	 
}
