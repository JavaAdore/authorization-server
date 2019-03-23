package com.eltaieb.microservice.base.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class UserLogin implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long userId;
	
	private Long userLoginId;
}
