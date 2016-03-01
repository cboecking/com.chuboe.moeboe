package com.chuboe.moeboe.po.api;

/**
 * 
 */
public interface DocumentPO<T extends RecordDTO> extends RecordPO<T> {
	
	/**
	 * 
	 */
	T processStatusWorkflow(Class<T> clazz, String collection, T t, String action) throws Exception;
	
}
