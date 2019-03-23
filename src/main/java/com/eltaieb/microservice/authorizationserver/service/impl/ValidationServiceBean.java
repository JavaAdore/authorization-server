package com.eltaieb.microservice.authorizationserver.service.impl;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.eltaieb.microservice.authorizationserver.config.ServiceConstant;
import com.eltaieb.microservice.authorizationserver.entity.UserLoginEntity;
import com.eltaieb.microservice.authorizationserver.entity.UserVerificationEntity;
import com.eltaieb.microservice.authorizationserver.enumuration.AuthenticationChannel;
import com.eltaieb.microservice.authorizationserver.enumuration.VerificationStatus;
import com.eltaieb.microservice.authorizationserver.model.BaseUserInfo;
import com.eltaieb.microservice.authorizationserver.model.ChangePasswordModel;
import com.eltaieb.microservice.authorizationserver.model.EntityModel;
import com.eltaieb.microservice.authorizationserver.model.FacebookLoginModel;
import com.eltaieb.microservice.authorizationserver.model.GoogleLoginModel;
import com.eltaieb.microservice.authorizationserver.model.UserNamePasswordLoginModel;
import com.eltaieb.microservice.authorizationserver.service.api.UserLoginService;
import com.eltaieb.microservice.authorizationserver.service.api.ValidationService;
import com.eltaieb.microservice.authorizationserver.validation.ValidationConstants;
import com.eltaieb.microservice.base.exception.ServiceException;
import com.eltaieb.microservice.base.model.ErrorMessageCode;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;

@Service
public class ValidationServiceBean implements ValidationService {

	private UserLoginService userService;
	private PasswordEncoder passwordEncoder;

	public ValidationServiceBean(UserLoginService userService, PasswordEncoder passwordEncoder) {
		this.userService = userService;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public void validateRegisterNewPublicUser(BaseUserInfo baseUserInfo) throws ServiceException {
		validateUserUserName(baseUserInfo.getUserName());
		validateUserFirstName(baseUserInfo.getFirstName());
		validateUserPassword(baseUserInfo.getPassword());
		validateUserBirthdate(baseUserInfo.getBirthDate());
		validateIfUserAlreadyExists(baseUserInfo.getUserName());

	}

	private void validateUserFirstName(String firstName) throws ServiceException {
		validateUserFirstNameExistance(firstName);
		validateUserFirstNameSpecialCharacters(firstName);
	}

	private void validateUserFirstNameSpecialCharacters(String firstName) {

	}

	private void validateUserFirstNameExistance(String firstName) throws ServiceException {
		if (StringUtils.isEmpty(firstName)) {
			throw new ServiceException(ErrorMessageCode.PUBLIC_USER_FIRST_NAME_IS_REQIORED,
					new Object[] { ValidationConstants.MIN_ALLOWED_PUBLIC_USER_AGE });
		}
	}

	private void validateUserUserName(String userName) throws ServiceException {
		if (StringUtils.isEmpty(userName)) {
			throw new ServiceException(ErrorMessageCode.PUBLIC_USER_USER_NAME_IS_REQIORED);
		}

	}

	private void validateUserBirthdate(LocalDate birthDate) throws ServiceException {
		if (null != birthDate) {
			validateIfUserDataInTheFuture(birthDate);
			validateUserMinAge(birthDate);
		}

	}

	private void validateUserMinAge(LocalDate birthDate) throws ServiceException {
		if (calcualteUserAge(birthDate) < ValidationConstants.MIN_ALLOWED_PUBLIC_USER_AGE) {
			throw new ServiceException(ErrorMessageCode.PUBLIC_USER_AGE_IS_NOT_ALLOWED,
					new Object[] { ValidationConstants.MIN_ALLOWED_PUBLIC_USER_AGE });
		}
	}

	private void validateIfUserDataInTheFuture(LocalDate birthDate) throws ServiceException {
		if (birthDate.isAfter(LocalDate.now())) {
			throw new ServiceException(ErrorMessageCode.PUBLIC_USER_BIRTHDAY_COULD_NOT_BE_IN_THE_FUTURE);
		}
	}

	private int calcualteUserAge(LocalDate birthDate) {
		return (int) java.time.temporal.ChronoUnit.YEARS.between(birthDate, LocalDate.now());

	}

	@Override
	public void validateUserPassword(String password) throws ServiceException {

		validatePasswordExistance(password);
		validatePasswordLength(password);

	}

	private void validatePasswordLength(String password) throws ServiceException {

		validateUserPasswordIfItsOnlySpaces(password);
		validateUserPasswordMinAndMaxLength(password);

	}

	private void validateUserPasswordMinAndMaxLength(String password) throws ServiceException {
		int passwordLength = password.trim().length();
		if (passwordLength < ValidationConstants.USER_PASSWORD_MIN_LENGTH
				|| passwordLength > ValidationConstants.USER_PASSWORD_MAX_LENGTH) {
			throw new ServiceException(ErrorMessageCode.PUBLIC_USER_PASSWORD_LENGTH_ERROR, new Object[] {
					ValidationConstants.USER_PASSWORD_MIN_LENGTH, ValidationConstants.USER_PASSWORD_MAX_LENGTH });
		}
	}

	private void validateUserPasswordIfItsOnlySpaces(String password) throws ServiceException {
		int passwordLength = password.trim().length();
		if (passwordLength == 0) {
			throw new ServiceException(ErrorMessageCode.PUBLIC_USER_PASSWORD_COULD_NOT_BE_ONLY_SPACES);
		}
	}

	private void validatePasswordExistance(String password) throws ServiceException {
		if (null == password) {
			throw new ServiceException(ErrorMessageCode.PUBLIC_USER_PASSWORD_IS_REQIORED);
		}
	}

	private void validateIfUserAlreadyExists(String email) throws ServiceException {
		List<UserLoginEntity> userLoginEntities = userService.findUsersByUserName(email);

		sortByStrongLevel(userLoginEntities);

		for (UserLoginEntity userLoginEntity : userLoginEntities) {
			if (isVerified(userLoginEntity.getVerificationStatus())) {
				throw new ServiceException(ErrorMessageCode.PUBLIC_USER_EMAIL_ALREADY_REGISTERED);
			} else {
				throw new ServiceException(ErrorMessageCode.DOUBLICATE_USERNAME_OWNERSHIP_PRETENTION);

			}
		}

	}

	private void sortByStrongLevel(List<UserLoginEntity> userLoginEntities) {
		userLoginEntities.sort((e1, e2) -> e1.getVerificationStatus().getStrongLevel()
				.compareTo(e1.getVerificationStatus().getStrongLevel()));

	}

	@Override
	public boolean isVerified(VerificationStatus verificationStatus) {
		return verificationStatus == VerificationStatus.VERIFIED;
	}

	@Override
	public void validate(FacebookLoginModel facebookLoginModel) throws ServiceException {

		if (null == facebookLoginModel || StringUtils.isEmpty(facebookLoginModel.getFacebookAccessToken())) {
			throw new ServiceException(ErrorMessageCode.NO_FACEBOOK_ACCESS_TOKE_PROVIDED);

		}
	}

	@Override
	public void validate(GoogleLoginModel googleLoginModel) throws ServiceException {

		if (null == googleLoginModel || StringUtils.isEmpty(googleLoginModel.getGoogleClientId())) {
			throw new ServiceException(ErrorMessageCode.NO_GOOGLE_CLIENT_ID_TOKEN_PROVIDED);

		}

	}

	@Override
	public void validate(UserNamePasswordLoginModel userNamePasswordLoginModel) throws ServiceException {

		if (null == userNamePasswordLoginModel || StringUtils.isEmpty(userNamePasswordLoginModel.getUserName())) {
			throw new ServiceException(ErrorMessageCode.PUBLIC_USER_USER_NAME_IS_REQIORED);
		}

		if (null == userNamePasswordLoginModel || StringUtils.isEmpty(userNamePasswordLoginModel.getPassword())) {
			throw new ServiceException(ErrorMessageCode.PUBLIC_USER_PASSWORD_IS_REQIORED);
		}
	}

	@Override
	public void validate(GoogleIdToken googleIdToken) throws ServiceException {

		if (null == googleIdToken) {
			throw new ServiceException(ErrorMessageCode.GOOGLE_CLIENT_ID_TOKEN_IS_EXPIRED);

		}
	}

	@Override
	public void validateTokenExpiry(UserVerificationEntity userVerificationEntity) throws ServiceException {

		validateIfTokenAlreadyConsumed(userVerificationEntity);
		validateIfTokenNotExpired(userVerificationEntity);
	}

	private void validateIfTokenNotExpired(UserVerificationEntity userVerificationEntity) throws ServiceException {
		if (userVerificationEntity.getTokenExpiryDate().isBefore(LocalDateTime.now())) {
			throw new ServiceException(ErrorMessageCode.EMAIL_TOKEN_HAS_BEEN_EXPIRED);
		}
	}

	private void validateIfTokenAlreadyConsumed(UserVerificationEntity userVerificationEntity) throws ServiceException {
		if (Boolean.TRUE == userVerificationEntity.getConsumed()) {
			throw new ServiceException(ErrorMessageCode.TOKEN_ALREADY_CONSUMED);
		}
	}

	@Override
	public void validateLastTokenSentDate(UserVerificationEntity userVerificationEntity) throws ServiceException {

		long minutesBetween = Duration.between(userVerificationEntity.getLastTokenSentDate(), LocalDateTime.now()).abs()
				.toMinutes();
		if (minutesBetween < ServiceConstant.MIN_DURATION_IN_MINUTES_BETWEEN_VERIFICATION_TOKEN_SEND_EMAIL) {
			long diff = ServiceConstant.MIN_DURATION_IN_MINUTES_BETWEEN_VERIFICATION_TOKEN_SEND_EMAIL - minutesBetween;
			throw new ServiceException(
					ErrorMessageCode.MIN_DURATION_IN_MINUTES_BETWEEN_VERIFICATION_TOKEN_SEND_EMAIL_VIOLATED,
					new Object[] { diff });
		}

	}

	@Override
	public void validateChangePassword(Long userLoginId, ChangePasswordModel changePasswordModel)
			throws ServiceException {
		Optional<UserLoginEntity> userLoginEntityOptional = userService.findUserLogin(userLoginId);
		if (userLoginEntityOptional.isPresent()) {
			UserLoginEntity userLoginEntity = userLoginEntityOptional.get();

			validateEligabilityToChangePassword(userLoginEntity);

			validateMatchingOldPassword(userLoginEntity.getPassword(), changePasswordModel.getOldPassword());

			validateMatchingOldAndNewPassword(userLoginEntity.getPassword(), changePasswordModel.getNewPassword());
			
			validatePasswordLength(changePasswordModel.getNewPassword());

		} else {
			throw new ServiceException(ErrorMessageCode.SOMETHING_WENT_WRONG);
		}

	}

	private void validateMatchingOldAndNewPassword(String hashedPassword, String noneHashedPassword) throws ServiceException {
		if ( hashedPassword.equals(passwordEncoder.encode(noneHashedPassword))) {
			throw new ServiceException(ErrorMessageCode.OLD_PASSWORD_AND_NEW_PASSWORD_SHOULD_NOT_BE_SAME);
		}		
	} 

	private void validateEligabilityToChangePassword(UserLoginEntity userLoginEntity) throws ServiceException {

		if (AuthenticationChannel.BASIC != userLoginEntity.getAuthenticationChannel()) {
			throw new ServiceException(ErrorMessageCode.ACCOUNT_TYPE_IS_NOT_ELIGIBLE_FOR_PASSWORD_CHANGE);

		}

	}

	private void validateMatchingOldPassword(String hashedPassword, String noneHashedPassword) throws ServiceException {

		if (Boolean.FALSE == hashedPassword.equals(passwordEncoder.encode(noneHashedPassword))) {
			throw new ServiceException(ErrorMessageCode.WRONG_OLD_PASSWORD);
		}
	}

	@Override
	public void validateRequestVerificationToken(String userName) throws ServiceException {
		
		validateUserUserName(userName);
		
		validateUserAlreadyVerified(userName);
		
		
	}

	private void validateUserAlreadyVerified(String userName) throws ServiceException {
		Optional<UserLoginEntity> userLoginEntityOptional =  	userService.findUserByVerifiedUserName(userName);
		if(userLoginEntityOptional.isPresent())
		{
			throw new ServiceException(ErrorMessageCode.USER_ALREADY_VERIFIED);

		}		
	}

	@Override
	public void validateRegisterNewSystemUser(String userName, Long entityId, List<String> roles)
			throws ServiceException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validateRegisterNewEntity(EntityModel entityModel) {
		// TODO Auto-generated method stub
		
	}

}
