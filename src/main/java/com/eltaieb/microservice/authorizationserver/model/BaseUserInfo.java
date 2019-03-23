package com.eltaieb.microservice.authorizationserver.model;

import java.io.Serializable;
import java.time.LocalDate;

import com.eltaieb.microservice.base.config.BaseServiceConstant;
import com.eltaieb.microservice.base.footprint.Referenceable;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class BaseUserInfo   implements Serializable , Referenceable {

	 



	/**
	 * 
	 */
	
	
	private static final long serialVersionUID = 1L;
	private String userName;
	
	
	private String password;

	private String firstName;

	private String lastName;

	private String gender;

	private String locale;
	
	
    
	@JsonFormat(pattern = BaseServiceConstant.DEFAULT_DATE_FORMAT)
	private LocalDate birthDate;



	@Override
	public String getReference() {
		return this.userName;
	}

	
}
