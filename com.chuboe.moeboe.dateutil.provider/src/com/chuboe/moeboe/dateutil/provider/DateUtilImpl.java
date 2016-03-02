package com.chuboe.moeboe.dateutil.provider;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import com.chuboe.moeboe.dateutil.api.DateUtil;

/**
 * 
 */
public class DateUtilImpl implements DateUtil{

	@Override
	public long nowEpochSecond() {
		return ZonedDateTime.now(ZoneOffset.UTC).toEpochSecond();
	}

	@Override
	public ZonedDateTime nowZonedDateTime() {
		return ZonedDateTime.now(ZoneOffset.UTC);
	}

	@Override
	public ZonedDateTime getDateTime(long epochSecond) {
		return ZonedDateTime.ofInstant(Instant.ofEpochSecond(epochSecond), ZoneOffset.UTC);
	}

	@Override
	public long getDateTime(ZonedDateTime zdt) {
		return zdt.toEpochSecond();
	}

}
