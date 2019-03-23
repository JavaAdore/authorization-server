package com.eltaieb.microservice.base.jpa.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class BooleanAttributeConverter implements AttributeConverter<Boolean, Integer> {

	@Override
	public Integer convertToDatabaseColumn(Boolean attribute) {
		if(null != attribute)
		{
			return attribute?1:0;
		}
		return null;
	}

	@Override
	public Boolean convertToEntityAttribute(Integer dbData) {
		if(null !=dbData)
		{
			return dbData.equals(1)?true:false;
		}
		return null;
	}
	
   
}
