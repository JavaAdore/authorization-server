package com.eltaieb.microservice.authorizationserver.model;

import java.util.List;

import lombok.Data;

@Data
public class SystemUserRegistrationInput {

	private String userName;
	private List<String> roles;
}
