package com.eltaieb.microservice.base.footprint;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalDateTime;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.bouncycastle.i18n.LocalizedException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.eltaieb.microservice.base.aspect.SecurityUtilityBean;
import com.eltaieb.microservice.base.config.BaseServiceConstant;
import com.eltaieb.microservice.base.exception.ServiceException;
import com.google.gson.Gson;

@Service
public class FootPrintService {

	private RabbitTemplate rabbitTemplate;

	public FootPrintService(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	@Async
	public void pushStartFootPrintEvent(ProceedingJoinPoint joinPoint, String requestId,
			LocalDateTime eventStartDateTime, LocalDateTime eventEndDateTime) {

		FootPrintMessage footPrintMessage = populateFootPrintMessage(joinPoint, requestId, eventStartDateTime,
				eventEndDateTime, FootPrintEventType.STARTING, null, null);
		pushToFootPrintQueue(footPrintMessage);

	}

	@Async
	public void pushSuccessFootPrintEvent(ProceedingJoinPoint joinPoint, String requestId,
			LocalDateTime eventStartDateTime, LocalDateTime eventEndDateTime, Object result) {

		FootPrintMessage footPrintMessage = populateFootPrintMessage(joinPoint, requestId, eventStartDateTime,
				eventEndDateTime, FootPrintEventType.SUCCESS, result, null);
		pushToFootPrintQueue(footPrintMessage);

	}

	@Async
	public void pushExceptionFootPrintEvent(ProceedingJoinPoint joinPoint, String requestId,
			LocalDateTime eventStartDateTime, LocalDateTime eventEndDateTime, Exception exception) {

		FootPrintMessage footPrintMessage = populateFootPrintMessage(joinPoint, requestId, eventStartDateTime,
				eventEndDateTime, extractFootPrintEventType(exception), null, exception);
		pushToFootPrintQueue(footPrintMessage);
	}

	private void pushToFootPrintQueue(FootPrintMessage footPrintMessage) {

		String gson = new Gson().toJson(footPrintMessage);
		rabbitTemplate.convertAndSend("logExchange", "footPrint", gson);

	}

	private FootPrintMessage populateFootPrintMessage(ProceedingJoinPoint joinPoint, String requestId,
			LocalDateTime eventStartDateTime, LocalDateTime eventEndDateTime, FootPrintEventType eventType,
			Object output, Exception exception) {

		String inputParamReference = null;
		String outputParamReference = null;
		if (eventType == FootPrintEventType.STARTING) {
			inputParamReference = extractInputParamReference(joinPoint);
		} else if (eventType == FootPrintEventType.SUCCESS) {
			outputParamReference = extractReference(output);
		} else if (eventType == FootPrintEventType.BUSINESS_EXCEPTION
				|| eventType == FootPrintEventType.UNHANDLE_EXCEPTION) {
			outputParamReference = extactExceptionReference(exception);
		}

		FootPrintMessage footPrintMessage = new FootPrintMessage();
		footPrintMessage.setRequestId(requestId);
		footPrintMessage.setUserId(getCurrentUserId());
		footPrintMessage.setUserLoginId(getUserLoginId());
		footPrintMessage.setEventName(extractEventName(joinPoint));
		footPrintMessage.setInputParamReference(inputParamReference);
		footPrintMessage.setEventType(eventType);
		footPrintMessage.setOutParamReference(outputParamReference);
		footPrintMessage.setEventStartDateTime(eventStartDateTime);
		footPrintMessage.setEventEndtDateTime(eventEndDateTime);
		if (null != eventStartDateTime && null != eventEndDateTime) {
			footPrintMessage.setEventDuration(Duration.between(eventStartDateTime, eventEndDateTime));
		}
		return footPrintMessage;

	}

	private Long getUserLoginId() {
		try {
			return SecurityUtilityBean.loadCurrentUserLoginId();
		} catch (Exception ex) {
			return BaseServiceConstant.UNAUTHENTICATED_USER_LOGIN_ID;
		}
	}

	private Long getCurrentUserId() {
		try {
			return SecurityUtilityBean.loadCurrentUserId();
		} catch (Exception ex) {
			return BaseServiceConstant.UNAUTHENTICATED_USER_ID;
		}
	}

	private String extactExceptionReference(Exception exception) {
		if (exception instanceof ServiceException) {
			ServiceException serviceException = (ServiceException) exception;
			if (serviceException.getErrorMessageCode() != null) {
				return serviceException.getErrorMessageCode().getMessageKey();
			}
		}
		if (exception instanceof LocalizedException) {
			LocalizedException localizedException = (LocalizedException) exception;
			return localizedException.getLocalizedMessage();
		}
		return exception.getMessage();
	}

	private FootPrintEventType extractFootPrintEventType(Exception exception) {
		if (exception instanceof ServiceException || exception instanceof LocalizedException) {
			return FootPrintEventType.BUSINESS_EXCEPTION;
		}
		return FootPrintEventType.UNHANDLE_EXCEPTION;
	}

	private String extractInputParamReference(ProceedingJoinPoint joinPoint) {
		if (null != joinPoint.getArgs()) {
			if (joinPoint.getArgs().length == 1) {
				Object param = joinPoint.getArgs()[0];
				return extractReference(param);
			}
		}
		return null;
	}

	private String extractReference(Object param) {
		if (param != null) {
			if (param instanceof Referenceable) {
				Referenceable referenceable = (Referenceable) param;
				return referenceable.getReference();
			} else if (param instanceof String) {
				return (String) param;
			} else if (param.getClass().isPrimitive()) {
				return String.valueOf(param);
			}

		}
		return null;
	}

	private String extractEventName(ProceedingJoinPoint joinPoint) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		Annotation annotation = method.getAnnotation(FootPrint.class);
		FootPrint footPrint = (FootPrint) annotation;
		return footPrint.value();
	}

}
