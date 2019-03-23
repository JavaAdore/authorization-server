package com.eltaieb.microservice.authorizationserver.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity(name="EntityEntity")
@Table(name="entity")
public class EntityEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id 
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator="entity_pk_seq")
	@SequenceGenerator(name="entity_pk_seq" , sequenceName="entity_pk_seq",allocationSize=1)
	private Long id;
	
	
	@Column(name="referenced_other_entity_id")
	private Long referencedOtherSystemEntityId;
	 
	@Column(name="referenced_other_entity_schema")
	private String referencedOtherEntitySchema;
	
	@Column(name="referenced_other_entity_table_name")
	private String referencedOtherEntityTableName;
	
	@Column(name="referenced_other_entity_type")
	private String referencedOtherSystemEntityType;
	
	
	
}
