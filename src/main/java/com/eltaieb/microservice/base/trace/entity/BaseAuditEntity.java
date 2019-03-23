package com.eltaieb.microservice.base.trace.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseAuditEntity {
	
	@CreatedBy
	@Column(name="created_by")
	private Long createdBy;
	
	@CreatedDate
	@Column(name="created_date")
	private LocalDateTime createDateTime;
	

}
