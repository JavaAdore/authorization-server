package com.eltaieb.microservice.authorizationserver.service.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.eltaieb.microservice.base.model.NotificationChannel;

import lombok.Data;

@Data
public class NotificationModel implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String code;
	final private Map<String,Object> parameters = new HashMap<>();
	private String to;
	private String locale;
	private NotificationChannel channel;
	private String referenceType;
	private String referenceNumber;

	public void addParameter(String key,String value)
	{
		if(null !=key  && null != value)
		{
			parameters.put(key,value);
		}
	}
	
	public Object getParamValue(String key)
	{
		return parameters.get(key);
	}
}
