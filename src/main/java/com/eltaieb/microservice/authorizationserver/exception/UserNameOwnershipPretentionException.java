package com.eltaieb.microservice.authorizationserver.exception;

import org.springframework.security.core.AuthenticationException;

public class UserNameOwnershipPretentionException extends AuthenticationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserNameOwnershipPretentionException(String msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}

}
