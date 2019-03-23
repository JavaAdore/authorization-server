package com.eltaieb.microservice.base.trace;

import com.eltaieb.microservice.base.trace.entity.BaseAuditEntity;

public interface Auditable {

	
	BaseAuditEntity getAudit();
}
