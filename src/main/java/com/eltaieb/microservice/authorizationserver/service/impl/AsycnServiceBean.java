package com.eltaieb.microservice.authorizationserver.service.impl;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.eltaieb.microservice.authorizationserver.service.api.AsycnService;
import com.eltaieb.microservice.authorizationserver.service.model.NotificationModel;
import com.eltaieb.microservice.base.feignclient.NotificationService;

@Service
public class AsycnServiceBean implements AsycnService {

	private NotificationService notificationService;

	public AsycnServiceBean(NotificationService notificationService) {
		this.notificationService = notificationService;
	}

	@Async
	public void sendNotification(NotificationModel notificationModel) {
		notificationService.sendNotification(notificationModel);
	}

}
