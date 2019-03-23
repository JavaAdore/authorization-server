package com.eltaieb.microservice.base.exception;

import feign.FeignException;

public class LocalizedServiceException extends FeignException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String code;
	private String bundledMessage;
	
	 
	public LocalizedServiceException()
	{
		super(null,null);
	}
	public LocalizedServiceException(String code, String bundledMessage) {
		super(bundledMessage, null);
 		this.code = code;
		this.bundledMessage = bundledMessage;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getBundledMessage() {
		return bundledMessage;
	}
	public void setBundledMessage(String bundledMessage) {
		this.bundledMessage = bundledMessage;
	}

}
