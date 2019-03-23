package com.eltaieb.microservice.base.aspect;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Service;

import com.eltaieb.microservice.base.model.UserLogin;

 
@Service
public class SecurityUtilityBean {

	HttpServletRequest request;

	public SecurityUtilityBean(HttpServletRequest request) {
		this.request = request;
	}

	public Locale getRequesterLocale() {
		if (request != null) {
			String localeAsString = request.getHeader("locale");
			if (null != localeAsString) {
				return new Locale(localeAsString);
			}
		}
		return Locale.ENGLISH;
	}

	public static Long loadCurrentUserId() {
		try {
			UserLogin UserLogin = (UserLogin) ((OAuth2AuthenticationDetails) (SecurityContextHolder.getContext()
					.getAuthentication().getDetails())).getDecodedDetails();
			return new Long(UserLogin.getUserId());
		} catch (Exception ex) {
			throw new AccessDeniedException("unable to load user Id");
		}
	}
	
	
	public static Long getCurrentUserId() {

		try
		{
			return loadCurrentUserId();
		}catch(Exception ex)
		{
			return null;
		}
	}
	
	
	
	public static Long loadCurrentUserLoginId() {
		try {
			UserLogin UserLogin = (UserLogin) ((OAuth2AuthenticationDetails) (SecurityContextHolder.getContext()
					.getAuthentication().getDetails())).getDecodedDetails();
			return new Long(UserLogin.getUserLoginId());
		} catch (Exception ex) {
			throw new AccessDeniedException("unable to load user login Id");
		}
	}

}
