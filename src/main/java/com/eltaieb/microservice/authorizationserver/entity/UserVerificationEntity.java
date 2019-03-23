package com.eltaieb.microservice.authorizationserver.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.eltaieb.microservice.authorizationserver.enumuration.VerificationChannel;
import com.eltaieb.microservice.authorizationserver.enumuration.VerificationReason;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity(name="UserVerification")
@Table(name="user_verification")
public class UserVerificationEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id 
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator="user_verification_pk_seq"  )
	@SequenceGenerator(name="user_verification_pk_seq" , sequenceName="user_verification_pk_seq",allocationSize=1 )
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="user_login_id" , referencedColumnName="id")
	private UserLoginEntity userLogin;
	
	private String token;
	
	private String code;
	
	@Enumerated(EnumType.STRING)
	@Column(name="verification_channel")
	private VerificationChannel verificationChannel;
	
	@Enumerated(EnumType.STRING)
	@Column(name="verification_reason")
	private VerificationReason reason;
	
	
	@Column(name="token_expiry_date")
    private LocalDateTime tokenExpiryDate;
    
	@Column(name="last_token_sent_date")
    private LocalDateTime lastTokenSentDate;
	
	@Column(name="vertification_date")
    private LocalDateTime verificationDate;
	
	@Column(name="has_consumed")
	private Boolean consumed;
 
}
