package com.eltaieb.microservice.authorizationserver.config;

import java.util.List;
import java.util.Optional;

import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.eltaieb.microservice.authorizationserver.dao.JpaUserLoginDao;
import com.eltaieb.microservice.authorizationserver.entity.UserLoginEntity;
import com.eltaieb.microservice.authorizationserver.enumuration.VerificationReason;
import com.eltaieb.microservice.authorizationserver.exception.UserNameOwnershipPretentionException;
import com.eltaieb.microservice.authorizationserver.facade.AuthorizationServerFacade;

@Order(Integer.MIN_VALUE)
@Service
public class CustomAuthenticationProvider extends DaoAuthenticationProvider {

	private JpaUserLoginDao jpaUserLoginDao;
	private AuthorizationServerFacade authorizationServerFacade;

	public CustomAuthenticationProvider(JpaUserLoginDao jpaUserLoginDao,
			AuthorizationServerFacade authorizationServerFacade, UserDetailsService userDetailsService,
			PasswordEncoder passwordEncoder) {
		super.setUserDetailsService(userDetailsService);
		super.setPasswordEncoder(passwordEncoder);
		this.jpaUserLoginDao = jpaUserLoginDao;
		this.authorizationServerFacade = authorizationServerFacade;

	}

	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
		try {
			super.additionalAuthenticationChecks(userDetails, authentication);

		} catch (BadCredentialsException badCredentialsException) {
			Optional<UserLoginEntity> userLoginEntityOptional = jpaUserLoginDao
					.findVerifiedUserByUserName(userDetails.getUsername());
			if (Boolean.FALSE == userLoginEntityOptional.isPresent()) {

				String encodedPassword = getPasswordEncoder().encode(userDetails.getPassword());
				List<UserLoginEntity> entities = jpaUserLoginDao.findUserLoginEntities(userDetails.getUsername(),
						encodedPassword);

				if (Boolean.FALSE == entities.isEmpty()) {
					
					entities.parallelStream().forEach(ul->
					{
						authorizationServerFacade.sendVerificationToken(ul, VerificationReason.USERNAME_OWNERSHIP);
					});
					throw new UserNameOwnershipPretentionException("kindly verify username ownership");
				}
			}
			throw badCredentialsException;
		}
	}

}
