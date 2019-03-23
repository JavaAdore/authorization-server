package com.eltaieb.microservice.authorizationserver.model;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Role implements Serializable{

	private static final long serialVersionUID = 1L;
	private String code;
	
	public Role(String code) {
		super();
		this.code = code;
	}
	 
 }
