package com.eltaieb.microservice.authorizationserver.enumuration;

public enum VerificationReason {

	USERNAME_OWNERSHIP("VERIFICATION_REASON__USERNAME_OWNERSHIP")
	,FORGET_PASSWORD("VERIFICATION_REASON__FORGET_PASSWORD");

	
	private VerificationReason(String notificationKey)
	{
		this.notificationKey=notificationKey;
	}
	
	private final String notificationKey;
	
	public String getNotificationKey() {
	 
		return notificationKey;
	}
}
