package com.eltaieb.microservice.base.aspect;

import java.util.Locale;
import java.util.logging.Level;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.ExpiredAuthorizationException;
import org.springframework.social.InvalidAuthorizationException;
import org.springframework.stereotype.Service;

import com.eltaieb.microservice.authorizationserver.exception.UserNameOwnershipPretentionException;
import com.eltaieb.microservice.base.exception.ServiceException;
import com.eltaieb.microservice.base.model.ErrorMessageCode;
import com.eltaieb.microservice.base.model.ServiceResponse;
import com.eltaieb.microservice.base.model.SuccessServiceResponse;

import lombok.extern.java.Log;
@Log
@Aspect
@Service
@Order(1)
public class ServiceRestControllerAspect {

	@Autowired
	SecurityUtilityBean securityUtilityBean;
	@Autowired
	private MessageSource messageSource;
	
	@SuppressWarnings("rawtypes")
	@Around("execution(* com.eltaieb.microservice.rest.controller.*.*(..))")
	public Object enhanceResponse(ProceedingJoinPoint joinPoint) throws Throwable {
		try {
			Object obj = joinPoint.proceed();
			if (obj instanceof SuccessServiceResponse) {
				setFeedMessageAsPerRequesterLocale((SuccessServiceResponse) obj);
			} 
			return obj;
		} catch (Exception ex) {

			ServiceException serviceException = null;
			if (ex instanceof ServiceException) {
				 serviceException = (ServiceException) ex;
			}else if(ex instanceof AuthenticationException)
			{
				AuthenticationException authenticationException = (AuthenticationException)ex;
				 serviceException = toApproperateServiceException(authenticationException );
 			}else if (ex instanceof InvalidAuthorizationException)
 			{
 				serviceException = new ServiceException(ErrorMessageCode.ERROR_WHILE_AUTHENTICATING_WITH_FACEBOOK);

 			}else if(ex instanceof ExpiredAuthorizationException)
 			{ 
 				serviceException = new ServiceException(ErrorMessageCode.FACEBOOK_TOKEN_IS_EXPIRED);
 			}
 			else
 			{
 				log.log(Level.WARNING,ex.getMessage(),ex);
 				serviceException = new ServiceException(ErrorMessageCode.GENERAL_BACKEND_ERROR);
 			}
			
			return prepareFailureRestServiceResponse(serviceException);

		}
	}

	@SuppressWarnings("rawtypes")
	private void setFeedMessageAsPerRequesterLocale(SuccessServiceResponse successServiceResponse) {
		String messageKey = successServiceResponse.getMessage();
		String message = getMessage(messageKey);
		successServiceResponse.setMessage(message);
	}

	private String getMessage(String messageKey, Object... params) {
		try {
			Locale requesterLocale = securityUtilityBean.getRequesterLocale();

			return messageSource.getMessage(messageKey, params, requesterLocale);

		} catch (Exception ex) {
			return messageKey;
		}
	}

	private Object prepareFailureRestServiceResponse(ServiceException serviceException) {

		String message = getMessage(serviceException.getMessageKey(), serviceException.getParams());

		return new ServiceResponse<>(serviceException.getCode(), message);

	}
	
	
	private ServiceException toApproperateServiceException(AuthenticationException ex) {
		
		if(ex instanceof UsernameNotFoundException)
		{
			return  new ServiceException(ErrorMessageCode.USER_NAME_NOT_FOUND);
		}
		if(ex instanceof BadCredentialsException)
		{
			return  new ServiceException(ErrorMessageCode.INVALID_USER_NAME_OR_PASSWORD);
		}
		if(ex instanceof LockedException)
		{
			return  new ServiceException(ErrorMessageCode.ACCOUNT_IS_LOCKED);
		}
		if(ex instanceof DisabledException)
		{
			return  new ServiceException(ErrorMessageCode.ACCOUNT_IS_DISABLED);
		}
		if(ex instanceof AccountExpiredException)
		{
			return  new ServiceException(ErrorMessageCode.ACCOUNT_IS_EXPIRED);
		}
		if(ex instanceof UserNameOwnershipPretentionException)
		{
			return  new ServiceException(ErrorMessageCode.DOUBLICATE_USERNAME_OWNERSHIP_PRETENTION);

		}
		
		return  new ServiceException(ErrorMessageCode.AUTHENTICATION_ERROR);
 
	}

}
