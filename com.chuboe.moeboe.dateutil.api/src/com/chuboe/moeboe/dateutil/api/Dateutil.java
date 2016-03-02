package com.chuboe.moeboe.dateutil.api;

import java.time.ZonedDateTime;

/**
 * Date Utils
 * All times are assumed in UTC
 */
public interface Dateutil {
	
	/**
	 * Assumes UTC 
	 */
	long nowEpochSecond();
	ZonedDateTime nowZonedDateTime();
	ZonedDateTime getDateTime(long epochSecond);
	long getDateTime(ZonedDateTime zdt);
}
