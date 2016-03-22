package com.chuboe.moeboe.recordvalidate.api;

/**
 * 
 */
public interface RecordValidate<T> {
	
	/**
	 * 
	 */
	T validate(T record, String recordPOAction);
	
	String RECORD_VALIDATE_CONFIG_NAME = "ValidateDTO";
}