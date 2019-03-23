package com.eltaieb.microservice.authorizationserver.model;

import com.eltaieb.microservice.base.footprint.Referenceable;

import lombok.Data;

@Data
public class UserNamePasswordLoginModel implements Referenceable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userName;
	private String password;
	
	@Override
	public String getReference() {
		return userName;
	}
}
