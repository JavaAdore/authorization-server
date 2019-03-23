package com.eltaieb.microservice.authorizationserver.service.impl;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerEndpointsConfiguration;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.stereotype.Service;

import com.eltaieb.microservice.authorizationserver.config.ServiceConstant;
import com.eltaieb.microservice.authorizationserver.model.Role;
import com.eltaieb.microservice.base.feignclient.UserRoleService;

@Service
public class AccessTokenGeneratorBean {
	
	 
	private final ServiceConstant serviceConstant;
	
	 
	private final  AuthorizationServerEndpointsConfiguration configuration;
	
	private final UserRoleService userRoleService;
	
	public final static List<String> ACCESS_SCOPES = Arrays.asList("read", "write");
 	
	public AccessTokenGeneratorBean(
			ServiceConstant serviceConstant ,
			AuthorizationServerEndpointsConfiguration configuration,
			UserRoleService userRoleService) {
		this.serviceConstant=serviceConstant;
		this.configuration=configuration;
		this.userRoleService=userRoleService;
		
	}
	
	
	
	public OAuth2AccessToken generateOAuth2AccessToken(User user) {

		Set<String> responseTypes = new HashSet<String>();
		responseTypes.add("code");
		List<GrantedAuthority> authorities = getRolesForPublicUsers();
		OAuth2Request oauth2Request = new OAuth2Request(new HashMap<String, String>(), serviceConstant.getClientId(),
				authorities, true, new HashSet<String>(ACCESS_SCOPES),
				new HashSet<String>(Arrays.asList("resourceIdTest")), null, responseTypes,
				new HashMap<String, Serializable>());

		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, "",
				authorities);
 
		OAuth2Authentication auth = new OAuth2Authentication(oauth2Request, authenticationToken);

		AuthorizationServerTokenServices tokenService = configuration.getEndpointsConfigurer().getTokenServices();

		OAuth2AccessToken token = tokenService.createAccessToken(auth);

		return token;

	}



	private List<GrantedAuthority> getRolesForPublicUsers() {
		List<Role> roles=  userRoleService.getRolesForPublicUsers();
		return roles.stream().map(r->new SimpleGrantedAuthority(r.getCode())).collect(Collectors.toList());
	}
}
