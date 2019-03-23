package com.eltaieb.microservice.authorizationserver.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.eltaieb.microservice.authorizationserver.enumuration.VerificationReason;
import com.eltaieb.microservice.base.model.NotificationTemplateCode;

@Service
public class NotificationTemplateCodeStore {

	Map<String,NotificationTemplateCode> notificationTemplateMap = new HashMap<>();
	

	@PostConstruct
	public void init()
	{
		notificationTemplateMap.put(VerificationReason.USERNAME_OWNERSHIP.getNotificationKey(), NotificationTemplateCode.VERIFY_USERNAME);
		notificationTemplateMap.put(VerificationReason.FORGET_PASSWORD.getNotificationKey(), NotificationTemplateCode.FORGET_PASSWORD);
	}
	
	public String getNotificationCode(VerificationReason reason) {

		 NotificationTemplateCode notificationTemplateCode=	notificationTemplateMap.get(reason.getNotificationKey());
		 if(null != notificationTemplateCode)
		 {
			 return notificationTemplateCode.getCode();
		 }
		return null;
	}

	
	
}
