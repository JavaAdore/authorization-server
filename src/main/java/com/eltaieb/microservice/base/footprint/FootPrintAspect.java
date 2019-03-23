package com.eltaieb.microservice.base.footprint;

import java.time.LocalDateTime;
import java.util.UUID;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

 
 
@Aspect
@Service
@Order(2)
public class FootPrintAspect {

	private FootPrintService footPrintService;

	public FootPrintAspect(FootPrintService footPrintService) {
		this.footPrintService = footPrintService;
	}

	@Around("@annotation(com.eltaieb.microservice.base.footprint.FootPrint)")
	public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
		String requestId= UUID.randomUUID().toString();
		LocalDateTime eventStartDateTime =  LocalDateTime.now();

		try 
		{
			footPrintService.pushStartFootPrintEvent(joinPoint ,requestId,eventStartDateTime,null);
			Object result =  joinPoint.proceed();
			footPrintService.pushSuccessFootPrintEvent(joinPoint,requestId,eventStartDateTime , LocalDateTime.now(),result);
			return result;
		} catch (Exception exception) {
			
			footPrintService.pushExceptionFootPrintEvent(joinPoint, requestId, eventStartDateTime ,LocalDateTime.now(),exception);
			throw exception;
		}
	}
}
