package com.eltaieb.microservice.authorizationserver.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.eltaieb.microservice.authorizationserver.enumuration.AuthenticationChannel;
import com.eltaieb.microservice.authorizationserver.enumuration.VerificationStatus;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity(name="UserLogin")
@Table(name="user_login")
public class UserLoginEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id 
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator="user_login_pk_seq")
	@SequenceGenerator(name="user_login_pk_seq" , sequenceName="user_login_pk_seq",allocationSize=1)
	private Long id;
	
	private Long userId;

	private String userName;
	
	private String password;

    @Enumerated(EnumType.STRING)
	private AuthenticationChannel authenticationChannel;
	
    @Enumerated(EnumType.STRING)
    @Column(name="verification_status")
    private VerificationStatus verificationStatus;
    
    @Column(name="is_permanently_locked")
    private Boolean permanentlyLocked;
 
}
