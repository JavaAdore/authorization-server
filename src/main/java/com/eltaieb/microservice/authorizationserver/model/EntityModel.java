package com.eltaieb.microservice.authorizationserver.model;

import lombok.Data;

@Data
public class EntityModel {

	
	
	private String referencedOtherEntitySchema;
	
	private String referencedOtherEntityTableName;
	
	private String referencedOtherSystemEntityType;
	
	private Long referencedOtherSystemEntityId;
	
}
