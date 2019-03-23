package com.eltaieb.microservice.base.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.eltaieb.microservice.authorizationserver.service.model.NotificationModel;
import com.eltaieb.microservice.base.feignclient.config.ContentExtractorFeignConfiuration;

@FeignClient(name="NOTIFICATION-SERVICE",configuration=ContentExtractorFeignConfiuration.class)
public interface NotificationService {

	
	@RequestMapping(path ="notification/send" , method=RequestMethod.POST)
	 void sendNotification(@RequestBody  NotificationModel verificationStatusModel);

 
	 
}
