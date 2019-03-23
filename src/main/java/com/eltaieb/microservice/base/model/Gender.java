package com.eltaieb.microservice.base.model;

public enum Gender {

	MALE("MALE"), FEMALE("FEMALE");

	private String genderAsString;

	private Gender(String genderAsString) {
		this.genderAsString = genderAsString;
	}

	public static Gender fromString(String gender) {
		if (null != gender) {
			for (Gender g : values()) {
				if (g.getGenderAsString().toUpperCase().equals(gender.toUpperCase()))
				{
					return g;
				}
			}
		}
		return null;
	}

	public String getGenderAsString() {
		return genderAsString;
	}
}
