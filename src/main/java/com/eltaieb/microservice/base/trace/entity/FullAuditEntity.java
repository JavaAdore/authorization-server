package com.eltaieb.microservice.base.trace.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;

import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FullAuditEntity  extends BaseAuditEntity {

	
	@LastModifiedBy
	@Column(name="last_update_by")
	private Long updatedBy;
	
	@LastModifiedDate
	@Column(name="last_update_date")
	private LocalDateTime lastUpdatedDate;

}
