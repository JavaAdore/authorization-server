package com.eltaieb.microservice.authorizationserver.service.model;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class CustomUserDetails extends User {


    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private Long userId;
	
	private String locale;
	
	private Long userLoginId;
	
	private boolean verified;
	
    public CustomUserDetails(Long userId , Long userLoginId ,boolean verified, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.userId = userId;
        this.userLoginId =userLoginId;
        this.verified=verified;
        
    }


}