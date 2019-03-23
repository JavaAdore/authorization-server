package com.eltaieb.microservice.base.footprint;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class FootPrintMessage implements Serializable { 
	
	private static final long serialVersionUID = 1L;
	private String requestId;
	private Long userId;
	private Long userLoginId  ;
	private String eventName  ;
	private String inputParamReference;
	private FootPrintEventType eventType ;
	private String outParamReference;
	private LocalDateTime eventStartDateTime;
	private LocalDateTime eventEndtDateTime;
	private Duration eventDuration;


}
