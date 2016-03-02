package com.chuboe.moeboe.po.api;

/**
 * 
 */
public interface DocumentPO<T extends RecordDTO> extends RecordPO<T> {
	
	/**
	 * 
	 */
	T processStatusWorkflow(Class<T> clazz, String collection, T t, String action) throws Exception;
	String DOCUMENTPO_ACTION_PREPARE = "com.chuboe.moeboe.documentpo.action.prepare";
	String DOCUMENTPO_ACTION_COMPLETE = "com.chuboe.moeboe.documentpo.action.complete";
	String DOCUMENTPO_ACTION_REVERSE_CORRECT = "com.chuboe.moeboe.documentpo.action.reverse_correct";
	String DOCUMENTPO_ACTION_REVERSE_ACCRUE = "com.chuboe.moeboe.documentpo.action.accrue";
	String DOCUMENTPO_ACTION_REVERSE_VOID = "com.chuboe.moeboe.documentpo.action.void";
}
