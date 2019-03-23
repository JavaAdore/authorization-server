package com.eltaieb.microservice.authorizationserver.service.model;

import java.io.Serializable;
import java.time.LocalDate;

import com.eltaieb.microservice.base.model.Gender;

import lombok.Data;

@Data
public class UserModel implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String firstName;
	
	private String lastName;
	
	private LocalDate birthDate;
	
	private String profilePictureUrl;
	
	private Gender gender;
	
}
