package com.eltaieb.microservice.base.trace.aware;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import com.eltaieb.microservice.base.aspect.SecurityUtilityBean;

@Component
public class AuditBeanConfig implements AuditorAware<Long> {

	public Optional<Long> getCurrentAuditor() {

	    return   Optional.ofNullable( SecurityUtilityBean.getCurrentUserId());
	  }
}
