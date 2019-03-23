package com.eltaieb.microservice.authorizationserver.enumuration;

public enum VerificationStatus {

	THIRD_PARTY(9), VERIFIED(10), NOT_VERIFIED(1), MUST_VERIFY(2);

	private int strongLevel;

	VerificationStatus(int strongLevel) {
		this.strongLevel = strongLevel;
	}
	
	
	public Integer getStrongLevel()
	{
		return strongLevel;
	}
}
