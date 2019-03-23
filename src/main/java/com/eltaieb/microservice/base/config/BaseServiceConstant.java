package com.eltaieb.microservice.base.config;

import java.util.Locale;

import org.springframework.stereotype.Service;

import lombok.Getter;

@Getter
@Service
public class BaseServiceConstant {
	
	private String DEFAULT_LOCALE=Locale.ENGLISH.getLanguage();
	
	public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

	public static final String GLOBAL_SUCCESS_CODE = "00";
 
	public static final Long UNAUTHENTICATED_USER_ID = -1L;

	public static final Long UNAUTHENTICATED_USER_LOGIN_ID = -1L;
 
}
