package com.eltaieb.microservice.base.model;

public class SuccessServiceResponse<T> extends ServiceResponse<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
 	public SuccessServiceResponse(T body) {
		super(ErrorMessageCode.OPERATION_COMPLETED_SUCCESSFULLY.getCode(), ErrorMessageCode.OPERATION_COMPLETED_SUCCESSFULLY.getMessageKey(), body);
	}
	
 

}
