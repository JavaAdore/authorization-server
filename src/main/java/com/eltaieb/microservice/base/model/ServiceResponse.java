package com.eltaieb.microservice.base.model;

import java.io.Serializable;

import com.eltaieb.microservice.base.config.BaseServiceConstant;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ServiceResponse<T> implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private String code;
	private String message;
	private T content;
	
	
	public ServiceResponse(String code, String message)
	{
		this(code,message,null);
	}
	public ServiceResponse(String code, String message, T content) {
		super();
		this.code = code;
		this.message = message;
		this.content = content;
	}
	@JsonIgnore
	public boolean isSuccess()
	{
		return BaseServiceConstant.GLOBAL_SUCCESS_CODE.equals(code);
	}
	
	
	
}
