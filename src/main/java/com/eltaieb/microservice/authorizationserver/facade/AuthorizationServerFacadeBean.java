package com.eltaieb.microservice.authorizationserver.facade;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.social.facebook.api.User;
import org.springframework.social.instagram.api.InstagramProfile;
import org.springframework.stereotype.Service;

import com.eltaieb.microservice.authorizationserver.config.ServiceConstant;
import com.eltaieb.microservice.authorizationserver.encoder.CustomPasswordEncoder;
import com.eltaieb.microservice.authorizationserver.entity.EntityEntity;
import com.eltaieb.microservice.authorizationserver.entity.UserLoginEntity;
import com.eltaieb.microservice.authorizationserver.entity.UserVerificationEntity;
import com.eltaieb.microservice.authorizationserver.enumuration.VerificationChannel;
import com.eltaieb.microservice.authorizationserver.enumuration.VerificationReason;
import com.eltaieb.microservice.authorizationserver.enumuration.VerificationStatus;
import com.eltaieb.microservice.authorizationserver.model.BaseUserInfo;
import com.eltaieb.microservice.authorizationserver.model.ChangePasswordModel;
import com.eltaieb.microservice.authorizationserver.model.EntityModel;
import com.eltaieb.microservice.authorizationserver.model.FacebookLoginModel;
import com.eltaieb.microservice.authorizationserver.model.GoogleLoginModel;
import com.eltaieb.microservice.authorizationserver.model.ResetPasswordModel;
import com.eltaieb.microservice.authorizationserver.model.Role;
import com.eltaieb.microservice.authorizationserver.model.SystemUserRegistrationInput;
import com.eltaieb.microservice.authorizationserver.model.UserNamePasswordLoginModel;
import com.eltaieb.microservice.authorizationserver.service.api.EntityService;
import com.eltaieb.microservice.authorizationserver.service.api.UserIdentityVerificationService;
import com.eltaieb.microservice.authorizationserver.service.api.UserLoginService;
import com.eltaieb.microservice.authorizationserver.service.api.ValidationService;
import com.eltaieb.microservice.authorizationserver.service.impl.AccessTokenGeneratorBean;
import com.eltaieb.microservice.authorizationserver.service.impl.AsycnServiceBean;
import com.eltaieb.microservice.authorizationserver.service.impl.NotificationTemplateCodeStore;
import com.eltaieb.microservice.authorizationserver.service.model.CustomUserDetails;
import com.eltaieb.microservice.authorizationserver.service.model.NotificationModel;
import com.eltaieb.microservice.authorizationserver.service.model.UserModel;
import com.eltaieb.microservice.base.aspect.SecurityUtilityBean;
import com.eltaieb.microservice.base.exception.ServiceException;
import com.eltaieb.microservice.base.feignclient.UserRoleService;
import com.eltaieb.microservice.base.feignclient.UserService;
import com.eltaieb.microservice.base.model.ErrorMessageCode;
import com.eltaieb.microservice.base.model.Gender;
import com.eltaieb.microservice.base.model.NotificationChannel;
import com.eltaieb.microservice.base.util.Utils;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;

@Service
public class AuthorizationServerFacadeBean implements AuthorizationServerFacade {

	private UserLoginService userLoginService;
	private AccessTokenGeneratorBean accessTokenGenerator;
	private ValidationService validationService;
	private UserService userService;
	private UserRoleService userRoleService;
	private CustomPasswordEncoder customPasswordEncoder;
	private UserIdentityVerificationService userIdentityVerificationService;
	private AsycnServiceBean asycnServiceBean;
	private SecurityUtilityBean securityUtilityBean;
	private NotificationTemplateCodeStore notificationTemplateCodeStore;
	private PasswordEncoder passwordEncoder;
	private EntityService entityService;
	public AuthorizationServerFacadeBean(UserLoginService userLoginService,
			AccessTokenGeneratorBean accessTokenGenerator, ValidationService validationService, UserService userService,
			UserRoleService userRoleService, CustomPasswordEncoder customPasswordEncoder,
			UserIdentityVerificationService userIdentityVerificationService, 
			NotificationTemplateCodeStore notificationTemplateCodeStore,
			SecurityUtilityBean securityUtilityBean,
			AsycnServiceBean asycnServiceBean,
			PasswordEncoder passwordEncoder,
			EntityService entityService) {
		super();
		this.userLoginService = userLoginService;
		this.accessTokenGenerator = accessTokenGenerator;
		this.validationService = validationService;
		this.userService = userService;
		this.userRoleService = userRoleService;
		this.customPasswordEncoder = customPasswordEncoder;
		this.userIdentityVerificationService = userIdentityVerificationService;
		this.notificationTemplateCodeStore=notificationTemplateCodeStore;
		this.securityUtilityBean=securityUtilityBean;
		this.asycnServiceBean = asycnServiceBean;
		this.passwordEncoder=passwordEncoder;
		this.entityService=entityService;
	}

	@Override
	public OAuth2AccessToken facebookLogin(User facebookUser) throws ServiceException {

		Optional<UserLoginEntity> userLoginOptional = userLoginService.findUserByFacebookId(facebookUser.getId());
		if (userLoginOptional.isPresent()) {
			UserModel userModel = toUserModel(facebookUser);
			updateUser(userLoginOptional.get().getId(), userModel);

			return generateAccessToken(userLoginOptional.get());
		}
		return registerNewUser(facebookUser);

	}

	private OAuth2AccessToken registerNewUser(User facebookUser) throws ServiceException {

		Long userId = addNewUser(toUserModel(facebookUser));
		List<Role> roles = AddPublicUserRolesToUser(userId);

		UserLoginEntity userLoginEntity = userLoginService.addOrUpdateUser(userId, facebookUser);

		CustomUserDetails customUserDetails = new CustomUserDetails(userLoginEntity.getUserId(), userLoginEntity.getId(),true, facebookUser.getId(),
				"", toGrantedAuthoroties(roles));

		return accessTokenGenerator.generateOAuth2AccessToken(customUserDetails);
	}

	private List<Role> AddPublicUserRolesToUser(Long userId) throws ServiceException {
		return userRoleService.AddPublicUserRolesToUser(userId);

	}

	private Long addNewUser(UserModel userModel) throws ServiceException {
		return userService.addUser(userModel);

	}

	private Collection<? extends GrantedAuthority> toGrantedAuthoroties(List<Role> roles) {
		return roles.stream().map(r -> new SimpleGrantedAuthority(r.getCode())).collect(Collectors.toList());
	}

	private UserModel toUserModel(User facebookUser) {
		UserModel userModel = new UserModel();
		userModel.setFirstName(facebookUser.getFirstName());
		userModel.setLastName(facebookUser.getLastName());
		userModel.setProfilePictureUrl(
				String.format(ServiceConstant.FACEBOOK_USER_PROFILE_PICTURE_URL_PATTERN, facebookUser.getId()));
		LocalDate birthDate = Utils.toDate(facebookUser.getBirthday(), ServiceConstant.FACEBOOK_DEFAULT_DATE_FORMAT);
		userModel.setBirthDate(birthDate);
		return userModel;
	}

	private OAuth2AccessToken generateAccessToken(UserLoginEntity userLoginEntity) {
		CustomUserDetails user = prepareCustomUserDetails(userLoginEntity);
		return accessTokenGenerator.generateOAuth2AccessToken(user);
	}

	private CustomUserDetails prepareCustomUserDetails(UserLoginEntity userEntity) {
		return new CustomUserDetails(userEntity.getUserId(), userEntity.getId() ,true,userEntity.getUserName(), "",
				prepareUserRoles(userEntity));
	}

	private Collection<? extends GrantedAuthority> prepareUserRoles(UserLoginEntity userEntity) {

		List<Role> userRoles = userRoleService.getUserRoles(userEntity.getUserId());
		HashSet<GrantedAuthority> roles = new HashSet<GrantedAuthority>();
		userRoles.forEach((r) -> {
			roles.add(new SimpleGrantedAuthority(r.getCode()));
		});

		return roles;
	}

	@Override
	public OAuth2AccessToken googleLogin(GoogleIdToken idToken) throws ServiceException {

		validationService.validate(idToken);
		Payload payload = idToken.getPayload();
		Optional<UserLoginEntity> userLoginOptional = userLoginService.findUserByGoogleId(payload.getSubject());

		if (userLoginOptional.isPresent()) {

			UserModel userModel = toUserModel(payload);
			updateUser(userLoginOptional.get().getId(), userModel);
			return generateAccessToken(userLoginOptional.get());
		}

		return registerNewUser(payload);

	}

	@Async
	private void updateUser(Long id, UserModel userModel) {
		userService.updateUser(id, userModel);
	}

	private OAuth2AccessToken registerNewUser(Payload payload) throws ServiceException {
		Long userId = addNewUser(toUserModel(payload));
		List<Role> roles = AddPublicUserRolesToUser(userId);

		UserLoginEntity userLoginEntity = userLoginService.addOrUpdateUser(userId, payload);

		CustomUserDetails customUserDetails = new CustomUserDetails(userLoginEntity.getUserId(),userLoginEntity.getId(),true, payload.getSubject(),
				"", toGrantedAuthoroties(roles));

		return accessTokenGenerator.generateOAuth2AccessToken(customUserDetails);
	}

	private UserModel toUserModel(Payload payload) {
		UserModel userModel = new UserModel();
		String fullName = (String) payload.get("name");
		String names[] = fullName.split(" ");
		userModel.setFirstName(names[0]);
		if (names.length == 2) {
			userModel.setLastName(names[1]);
		}
		userModel.setGender(Gender.fromString((String) payload.get("gender")));
		userModel.setProfilePictureUrl((String) payload.get("picture"));
		return userModel;
	}

	@Override
	public OAuth2AccessToken registerNewPublicUser(BaseUserInfo baseUserInfo) throws ServiceException {

		try {
			validationService.validateRegisterNewPublicUser(baseUserInfo);

			Long userId = addNewUser(toUserModel(baseUserInfo));

			List<Role> roles = AddPublicUserRolesToUser(userId);

			UserLoginEntity userLoginEntity = userLoginService.addOrUpdateUser(userId, baseUserInfo,
					VerificationStatus.NOT_VERIFIED);

			CustomUserDetails customUserDetails = new CustomUserDetails(userLoginEntity.getUserId(),userLoginEntity.getId(),false,
					baseUserInfo.getUserName(), "", toGrantedAuthoroties(roles));

			sendVerificationToken(userLoginEntity, VerificationReason.USERNAME_OWNERSHIP);

			return accessTokenGenerator.generateOAuth2AccessToken(customUserDetails);

		} catch (ServiceException serviceException) {
			if (ErrorMessageCode.DOUBLICATE_USERNAME_OWNERSHIP_PRETENTION
					.equals(serviceException.getErrorMessageCode())) {
				Optional<UserLoginEntity> userLoginEntityOptional = userLoginService
						.findNotVerifiedUserByUserName(baseUserInfo.getUserName());
				if (userLoginEntityOptional.isPresent() && userLoginEntityOptional.get().getPassword()
						.equals(customPasswordEncoder.encode(baseUserInfo.getPassword()))) {
					UserLoginEntity userLoginEntity = userLoginEntityOptional.get();
					updateUser(userLoginEntity.getId(), toUserModel(baseUserInfo));
					List<Role> roles = userRoleService.getUserRoles(userLoginEntity.getId());
					boolean verified = validationService.isVerified(userLoginEntity.getVerificationStatus());
					
					CustomUserDetails customUserDetails = new CustomUserDetails(userLoginEntity.getUserId(),userLoginEntity.getId(),verified,
							baseUserInfo.getUserName(), "", toGrantedAuthoroties(roles));
					return accessTokenGenerator.generateOAuth2AccessToken(customUserDetails);
				} else {

					Long userId = addNewUser(toUserModel(baseUserInfo));

					AddPublicUserRolesToUser(userId);

					UserLoginEntity userLoginEntity = userLoginService.addOrUpdateUser(userId, baseUserInfo,
							VerificationStatus.MUST_VERIFY);

					sendVerificationToken(userLoginEntity, VerificationReason.USERNAME_OWNERSHIP);
				}
			}

			throw serviceException;
		}
	}

	private UserModel toUserModel(BaseUserInfo baseUserInfo) {
		UserModel userModel = new UserModel();
		userModel.setFirstName(baseUserInfo.getFirstName());
		userModel.setLastName(baseUserInfo.getLastName());
		userModel.setGender(Gender.fromString(baseUserInfo.getGender()));
		return userModel;
	}

	@Override
	public void validate(FacebookLoginModel facebookLoginModel) throws ServiceException {
		validationService.validate(facebookLoginModel);
	}

	@Override
	public void validate(GoogleLoginModel googleLoginModel) throws ServiceException {
		validationService.validate(googleLoginModel);
	}

	@Override
	public void validate(UserNamePasswordLoginModel userNamePasswordLoginModel) throws ServiceException {
		validationService.validate(userNamePasswordLoginModel);
	}

	@Override
	public OAuth2AccessToken instagramLogin(InstagramProfile instagramProfile) throws ServiceException {
		Optional<UserLoginEntity> userLoginOptional = userLoginService
				.findUserByInstagramId(String.valueOf(instagramProfile.getId()));
		if (userLoginOptional.isPresent()) {
			UserModel userModel = toUserModel(instagramProfile);
			updateUser(userLoginOptional.get().getId(), userModel);

			return generateAccessToken(userLoginOptional.get());
		}
		return registerNewUser(instagramProfile);
	}

	private OAuth2AccessToken registerNewUser(InstagramProfile instagramProfile) throws ServiceException {
		Long userId = addNewUser(toUserModel(instagramProfile));
		List<Role> roles = AddPublicUserRolesToUser(userId);

		UserLoginEntity userLoginEntity = userLoginService.addOrUpdateUser(userId, instagramProfile);

		CustomUserDetails customUserDetails = new CustomUserDetails(userLoginEntity.getUserId(),userLoginEntity.getId(),true,
				String.valueOf(instagramProfile.getId()), "", toGrantedAuthoroties(roles));

		return accessTokenGenerator.generateOAuth2AccessToken(customUserDetails);
	}

	private UserModel toUserModel(InstagramProfile instagramProfile) {
		UserModel userModel = new UserModel();
		String fullName = instagramProfile.getFullName();
		String names[] = fullName.split(" ");
		userModel.setFirstName(names[0]);
		if (names.length == 2) {
			userModel.setLastName(names[1]);
		}
		userModel.setProfilePictureUrl(instagramProfile.getProfilePictureUrl());
		return userModel;
	}

	@Override
	public void verifyUserNameOwnershipByToken(String encodedToken) throws ServiceException {
		String decodedToken = decodeEmailToken(encodedToken);
		Optional<UserVerificationEntity> userVerificationEntityOptional = userIdentityVerificationService
				.findUserVerificationEntityByTokenAndReason(decodedToken, VerificationReason.USERNAME_OWNERSHIP);

		if (userVerificationEntityOptional.isPresent()) {

			UserVerificationEntity userVerificationEntity = userVerificationEntityOptional.get();

			verifyUserVerification(userVerificationEntity);

		} else {

			throw new ServiceException(ErrorMessageCode.UNKNOWN_VERIFICATION_TOKEN_PROVIDED);
		}

	}

	private void verifyUserVerification(UserVerificationEntity userVerificationEntity) throws ServiceException {

		doVerifyTokenValidation(userVerificationEntity);

		consumeVerification(userVerificationEntity);

		handleFraudCases(userVerificationEntity.getUserLogin());

		markUserAsVerified(userVerificationEntity.getUserLogin());
	}

	private void handleFraudCases(UserLoginEntity userLogin) {

		List<UserLoginEntity> usersLogin = userLoginService.findUsersByUserName(userLogin.getUserName());
		usersLogin.parallelStream().forEach(ul -> {
			if (Boolean.FALSE == ul.equals(userLogin)) {
				userLoginService.permenantlyBlockFraudCases(ul);
				// kick user
			}
		});

	}

	private void doVerifyTokenValidation(UserVerificationEntity userVerificationEntity) throws ServiceException {
		validateUserAlreadyVerified(userVerificationEntity.getUserLogin().getUserName());
		validateTokenExpiry(userVerificationEntity);
	}

	private void markUserAsVerified(UserLoginEntity userLogin) {
		userLogin.setVerificationStatus(VerificationStatus.VERIFIED);
		userLoginService.update(userLogin);
	}

	private void consumeVerification(UserVerificationEntity userVerificationEntity) {
		userVerificationEntity.setConsumed(Boolean.TRUE);
		userVerificationEntity.setVerificationDate(LocalDateTime.now());
		userIdentityVerificationService.update(userVerificationEntity);

	}

	private void validateUserAlreadyVerified(String userName) throws ServiceException {

		Optional<UserLoginEntity> userLoginEntityOptional = userLoginService.findUserByVerifiedUserName(userName);
		if (userLoginEntityOptional.isPresent()) {
			throw new ServiceException(ErrorMessageCode.USER_NAME_IS_ALREADY_VERIFIED);
		}
	}

	private boolean isVerified(VerificationStatus verificationStatus) {
		return validationService.isVerified(verificationStatus);
	}

	private void validateTokenExpiry(UserVerificationEntity userVerificationEntity) throws ServiceException {
		validationService.validateTokenExpiry(userVerificationEntity);
	}

	@Async("asyncExecutor")
	public  UserVerificationEntity sendVerificationToken(UserLoginEntity userLogin,
			VerificationReason reason) {
		UserVerificationEntity userVerificationEntity = generateUserVerificationEntity(userLogin, reason);
		asycnServiceBean.sendNotification(toNotificationModel(userVerificationEntity));
		return userVerificationEntity;
	}

	private NotificationModel toNotificationModel(UserVerificationEntity userVerificationEntity) {
		NotificationModel notificationModel = new NotificationModel();
		notificationModel.setTo(userVerificationEntity.getUserLogin().getUserName());
		notificationModel.setChannel(toNotificationChannel(userVerificationEntity.getVerificationChannel()));
		notificationModel.setCode( getNotificationCode(userVerificationEntity.getReason()));
		notificationModel.setLocale(securityUtilityBean.getRequesterLocale().getLanguage());
		String token = getApproperateToken(userVerificationEntity);
		notificationModel.addParameter("token", token);
		return notificationModel;
	}

	private String getNotificationCode(VerificationReason reason) {
		return notificationTemplateCodeStore.getNotificationCode(reason);
	}

	private String getApproperateToken(UserVerificationEntity userVerificationEntity) {

		if (VerificationChannel.SMS == userVerificationEntity.getVerificationChannel()) {
			return userVerificationEntity.getCode();
		}
		return userVerificationEntity.getToken();
	}

	private NotificationChannel toNotificationChannel(VerificationChannel verificationChannel) {
		// TODO refactor ... later on
		// TODO user mapper
		return NotificationChannel.valueOf(verificationChannel.name());
	}

	private UserVerificationEntity generateUserVerificationEntity(UserLoginEntity userLogin,
			VerificationReason reason) {
		return userIdentityVerificationService.generateUserVerificationEntity(userLogin, reason);
	}

	private String decodeEmailToken(String encodedToken) throws ServiceException {
		try {
			return URLDecoder.decode(encodedToken, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new ServiceException(ErrorMessageCode.INVALID_EMAIL_TOKEN_PROVIDED);
		}
	}
	
	private void validatePassword(String password) throws ServiceException {
		validationService.validateUserPassword(password);
	}

	@Override
	public void verifyUserNameOwnershipByCode(String code) throws ServiceException {
		Optional<UserVerificationEntity> userVerificationEntityOptional = userIdentityVerificationService
				.findUserVerificationEntityByCodeAndReason(code, VerificationReason.USERNAME_OWNERSHIP);

		if (userVerificationEntityOptional.isPresent()) {

			UserVerificationEntity userVerificationEntity = userVerificationEntityOptional.get();

			verifyUserVerification(userVerificationEntity);

		} else {

			throw new ServiceException(ErrorMessageCode.UNKNOWN_VERIFICATION_CODE_PROVIDED);
		}
	}

	@Override
	public void sendForgetPassword(String userName) throws ServiceException {

		Optional<UserLoginEntity> userLoginEntityOptional = userLoginService.findUserByVerifiedUserName(userName);
		if (userLoginEntityOptional.isPresent()) {
			UserLoginEntity userLoginEntity = userLoginEntityOptional.get();
			UserVerificationEntity userVerificationEntity = generateUserVerificationEntity(userLoginEntity,
					VerificationReason.FORGET_PASSWORD);
			asycnServiceBean.sendNotification(toNotificationModel(userVerificationEntity));
		}else
		{
			 userLoginEntityOptional = userLoginService.findNotVerifiedUserByUserName(userName);
			 if(userLoginEntityOptional.isPresent())
			 {
				 sendUserNameOwnershipVerificationToken(userLoginEntityOptional.get());
				throw new ServiceException(ErrorMessageCode.USER_IS_NOT_VERIFIED_VERIFY_FIRST);

			 }
		}
	}

	@Override
	public OAuth2AccessToken resetPasswordByCode(ResetPasswordModel resetPasswordModel) throws ServiceException {

		Optional<UserVerificationEntity> userVerificationEntityOptional = userIdentityVerificationService
				.findUserVerificationEntityByCodeAndReason(resetPasswordModel.getSecretValue(),
						VerificationReason.FORGET_PASSWORD);

		if (userVerificationEntityOptional.isPresent()) {

			return resetPassword(userVerificationEntityOptional.get(), resetPasswordModel);

		} else {

			throw new ServiceException(ErrorMessageCode.UNKNOWN_VERIFICATION_TOKEN_PROVIDED);
		}

	}

	@Override
	public OAuth2AccessToken resetPasswordByToken(ResetPasswordModel resetPasswordModel) throws ServiceException {

		Optional<UserVerificationEntity> userVerificationEntityOptional = userIdentityVerificationService
				.findUserVerificationEntityByTokenAndReason(resetPasswordModel.getSecretValue(),
						VerificationReason.FORGET_PASSWORD);
		if (userVerificationEntityOptional.isPresent()) {

			return resetPassword(userVerificationEntityOptional.get(), resetPasswordModel);

		} else {

			throw new ServiceException(ErrorMessageCode.UNKNOWN_VERIFICATION_TOKEN_PROVIDED);
		}
	}

	public OAuth2AccessToken resetPassword(UserVerificationEntity userVerificationEntity,
			ResetPasswordModel resetPasswordModel) throws ServiceException {

		validateTokenExpiry(userVerificationEntity);
		validatePassword(resetPasswordModel.getPassword());

		UserLoginEntity userLoginEntity = userVerificationEntity.getUserLogin();
		userLoginEntity.setPassword(customPasswordEncoder.encode(resetPasswordModel.getPassword()));
		userLoginEntity = userLoginService.update(userLoginEntity);
		consumeVerification(userVerificationEntity);
		return generateAccessToken(userLoginEntity);

	}

	@Override
	public void changePassword(Long userLoginId, ChangePasswordModel changePasswordModel) throws ServiceException {
		validationService.validateChangePassword(userLoginId,changePasswordModel);
		Optional<UserLoginEntity> userLoginEntityOptional = userLoginService.findUserLogin(userLoginId);
		UserLoginEntity userLoginEntity = userLoginEntityOptional.get();
		userLoginEntity.setPassword(customPasswordEncoder.encode(changePasswordModel.getNewPassword()));
		userLoginService.save(userLoginEntity);
	}

	@Override
	public void requestVerificationToken(String userName) throws ServiceException {
		
		
		validationService.validateRequestVerificationToken(userName);
		
		Optional<UserLoginEntity> userLoginEntityOptional = userLoginService.findNotVerifiedUserByUserName(userName);
		
		if (userLoginEntityOptional.isPresent()) {
			sendUserNameOwnershipVerificationToken(userLoginEntityOptional.get());
		}		
	}

	private void sendUserNameOwnershipVerificationToken(UserLoginEntity userLoginEntity) {
		UserVerificationEntity userVerificationEntity = generateUserVerificationEntity(userLoginEntity,
				VerificationReason.USERNAME_OWNERSHIP);
		asycnServiceBean.sendNotification(toNotificationModel(userVerificationEntity));		
	}

	@Override
	public void registerNewSystemUser(SystemUserRegistrationInput systemUserRegistrationInput, Long entityId) throws ServiceException
	{
		String userName = systemUserRegistrationInput.getUserName();
 		List<String> roles = systemUserRegistrationInput.getRoles();
 		
		validationService.validateRegisterNewSystemUser(userName,entityId,systemUserRegistrationInput.getRoles());
		
		boolean isUserNameRegistered= isUserNameRegistered(userName);
		
		if(Boolean.FALSE == isUserNameRegistered )
		{
			
			registerNewSystemUser(userName,roles,entityId);
			
		}else
		{
			
			handleAlreadyExistSystemUserRegistration(userName, roles,entityId);
			
		}
	
	
	}

	private void registerNewSystemUser(String userName, List<String> roles, Long entityId) {
		Long userId = 	userService.addNewSystemUser(userName);
		userService.assignUserToEntity(userId,entityId);
		userRoleService.addRolesToUser(userId,entityId,roles);
		BaseUserInfo baseUserInfo = new BaseUserInfo();
		baseUserInfo.setUserName(userName);
		baseUserInfo.setPassword(userName);
		UserLoginEntity userLoginEntity = userLoginService.addOrUpdateUser(userId, baseUserInfo,VerificationStatus.MUST_VERIFY);
			
		UserVerificationEntity userVerificationEntity =	sendVerificationToken(userLoginEntity, VerificationReason.USERNAME_OWNERSHIP);
		
		userLoginEntity.setPassword(passwordEncoder.encode(userVerificationEntity.getCode() ));

		userLoginService.save(userLoginEntity);
		
		
		
	
	}

	private void handleAlreadyExistSystemUserRegistration(String userName, List<String> roles, Long entityId) {
		
		Optional<UserLoginEntity> userLoginEntityOptional=   userLoginService.findLogingabledUserByUserName(userName);
		UserLoginEntity userLoginEntity =userLoginEntityOptional.get();
		userService.assignUserToEntity(userLoginEntity.getUserId(),entityId);
		userRoleService.addRolesToUser(userLoginEntity.getUserId(),entityId,roles);
	}
	

	private boolean isUserNameRegistered(String userName) {
		Optional<UserLoginEntity> userLoginEntityOptional = userLoginService.findLogingabledUserByUserName(userName);
		return userLoginEntityOptional.isPresent();
	}

	@Override
	public Long registerNewEntity(EntityModel entityModel) throws ServiceException {
		validationService.validateRegisterNewEntity(entityModel);
		EntityEntity entityEntity = entityService.save(toEntityEntity(entityModel));
		return entityEntity.getId();
	}

	private EntityEntity toEntityEntity(EntityModel entityModel) {
		EntityEntity entityEntity = new EntityEntity();
		entityEntity.setReferencedOtherEntitySchema(entityModel.getReferencedOtherEntitySchema());
		entityEntity.setReferencedOtherEntityTableName(entityModel.getReferencedOtherEntityTableName());
		entityEntity.setReferencedOtherSystemEntityId(entityModel.getReferencedOtherSystemEntityId());
		entityEntity.setReferencedOtherSystemEntityType(entityModel.getReferencedOtherSystemEntityType());
		return entityEntity;
	}

	

}
