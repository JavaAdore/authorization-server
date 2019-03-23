package com.eltaieb.microservice.base.model;

public enum NotificationTemplateCode {
	
	VERIFY_USERNAME("01"),
	FORGET_PASSWORD("02");

	
	
 	
	

 

	 

	 
	
 
	final private String code;

	NotificationTemplateCode(String code)
	{
		this.code= code;
 	}
	
	public String getCode() {
		return code;
	}
	
}
