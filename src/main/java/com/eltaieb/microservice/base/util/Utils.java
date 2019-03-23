package com.eltaieb.microservice.base.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Utils {

	public static LocalDate toDate(String dateAsString, String format) {
		try {
			LocalDate localDate = LocalDate.parse(dateAsString, DateTimeFormatter.ofPattern(format));
			 return localDate;
		} catch (Exception ex) {
			return null;
		}

	}
}
