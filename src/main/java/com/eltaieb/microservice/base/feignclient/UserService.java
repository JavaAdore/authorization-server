package com.eltaieb.microservice.base.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.eltaieb.microservice.authorizationserver.service.model.UserModel;
import com.eltaieb.microservice.base.feignclient.config.ContentExtractorFeignConfiuration;

@FeignClient(name="USER-SERVICE",configuration=ContentExtractorFeignConfiuration.class)
public interface UserService {

 	@RequestMapping(path ="administration/user/" , method=RequestMethod.POST)
	Long addUser(@RequestBody UserModel user);
 	
 	@RequestMapping(path ="administration/user/{userId}" , method=RequestMethod.PUT)
	void updateUser(@PathVariable("userId") Long userId ,  @RequestBody UserModel user);

 	@RequestMapping(path ="system-user-administration/add/{userName}" , method=RequestMethod.GET)
	Long addNewSystemUser(@PathVariable("userName") String userName);
 	
 	@RequestMapping(path ="system-user-administration/assign/{userId}/{entityId}" , method=RequestMethod.POST)
	Long assignUserToEntity(@PathVariable("userId") Long userId, @PathVariable("entityId")  Long entityId);
 	
 	
 	 
}
