package com.eltaieb.microservice.base.util;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eltaieb.microservice.base.feignclient.config.ContentExtractorFeignConfiuration;
import com.eltaieb.microservice.base.model.ServiceResponse;

@FeignClient(name="role-service",configuration=ContentExtractorFeignConfiuration.class)
public interface TestFeign {

	
	@RequestMapping("roles/test")
	public ServiceResponse<String>test();
}
