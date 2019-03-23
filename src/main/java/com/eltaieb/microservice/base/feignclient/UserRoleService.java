package com.eltaieb.microservice.base.feignclient;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.eltaieb.microservice.authorizationserver.model.Role;
import com.eltaieb.microservice.base.feignclient.config.ContentExtractorFeignConfiuration;

@FeignClient(name="ROLE-SERVICE",configuration=ContentExtractorFeignConfiuration.class)
public interface UserRoleService {

	
	@RequestMapping(path ="administration/public/addPublicUserRoles/{userId}" , method=RequestMethod.GET)
	List<Role> AddPublicUserRolesToUser(@PathVariable("userId") Long userId);

	@RequestMapping(path ="administration/public/{userId}" , method=RequestMethod.GET)
	List<Role> getUserRoles(@PathVariable("userId")  Long userId);
	
	@RequestMapping(path ="administration/public/roles" , method=RequestMethod.GET)
	List<Role> getRolesForPublicUsers();

	@RequestMapping(path ="administration/system/users/{userId}/{entityId}/" , method=RequestMethod.POST)
	void addRolesToUser(@PathVariable("userId") Long userId,@PathVariable("entityId") Long entityId, @RequestBody List<String> roles);
	
	 
}
