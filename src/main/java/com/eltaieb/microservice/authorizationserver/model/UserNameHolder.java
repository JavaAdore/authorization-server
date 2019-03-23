package com.eltaieb.microservice.authorizationserver.model;

import com.eltaieb.microservice.base.footprint.Referenceable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserNameHolder implements Referenceable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userName;

	
	@Override
	public String getReference() {
		return  this.getUserName();
	}
}
