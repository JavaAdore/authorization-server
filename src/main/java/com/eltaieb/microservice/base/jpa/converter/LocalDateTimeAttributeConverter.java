package com.eltaieb.microservice.base.jpa.converter;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class LocalDateTimeAttributeConverter implements AttributeConverter<LocalDateTime, Timestamp> {
	
    @Override
    public Timestamp convertToDatabaseColumn(LocalDateTime localeDateTime) {
    	
    	if(null == localeDateTime) return null;
    	ZonedDateTime zdt = localeDateTime.atZone(ZoneId.systemDefault());
    	long millis = zdt.toInstant().toEpochMilli();
    	Timestamp timestamp = new Timestamp(millis);
    	return timestamp;

    }

    @Override
    public LocalDateTime convertToEntityAttribute(Timestamp timestamp) {
    	if(null == timestamp) return null;
    	return timestamp.toLocalDateTime();
     }
}
