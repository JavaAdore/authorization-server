package com.eltaieb.microservice.authorizationserver.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.eltaieb.microservice.authorizationserver.dao.JpaUserLoginDao;
import com.eltaieb.microservice.authorizationserver.entity.UserLoginEntity;
import com.eltaieb.microservice.authorizationserver.enumuration.VerificationStatus;
import com.eltaieb.microservice.authorizationserver.model.Role;
import com.eltaieb.microservice.authorizationserver.service.model.CustomUserDetails;
import com.eltaieb.microservice.base.feignclient.UserRoleService;

 
@Service
public class CustomUserDetailsServiceBean implements UserDetailsService {
	
	
	private JpaUserLoginDao jpaUserLoginDao;
	private UserRoleService userRoleService;
 	public CustomUserDetailsServiceBean(JpaUserLoginDao jpaUserLoginDao , UserRoleService userRoleService) {
		super();
		this.jpaUserLoginDao = jpaUserLoginDao;
		this.userRoleService=userRoleService;
  	}

 
	
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
    	
    	Optional<UserLoginEntity> optional= 	jpaUserLoginDao.findLogingabledUserByUserName(userName);
    	if(optional.isPresent())
    	{
    		UserLoginEntity userEntity = optional.get();
    		 List<GrantedAuthority> grantedAuthorities= new ArrayList<>();
    		 
    		 List<Role> roles =userRoleService.getUserRoles(userEntity.getUserId());
    		
    		 roles.forEach(r->{
    			grantedAuthorities.add( new SimpleGrantedAuthority(r.getCode()));

    		});
    		 boolean verified = VerificationStatus.VERIFIED==userEntity.getVerificationStatus();
    		 
    		CustomUserDetails customUserDetails=  new CustomUserDetails(userEntity.getUserId(), userEntity.getId() ,verified , userName,userEntity.getPassword(), grantedAuthorities);
      		
    		return customUserDetails;
    	}
    	 
    	throw new UsernameNotFoundException(userName);
    	
     }
}