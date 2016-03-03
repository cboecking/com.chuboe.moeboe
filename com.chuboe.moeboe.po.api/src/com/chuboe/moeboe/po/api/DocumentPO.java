package com.chuboe.moeboe.po.api;

/**
 * 
 */
public interface DocumentPO<T extends RecordDTO> extends RecordPO<T> {
	
	/**
	 * 
	 */
	T processStatusWorkflow(Class<T> clazz, String collection, T t, String action) throws Exception;

	String DOCUMENTPO_ACTION_PREPARE = "com.chuboe.moeboe.po.documentpo.action.prepare";
	String DOCUMENTPO_ACTION_COMPLETE = "com.chuboe.moeboe.po.documentpo.action.complete";
	String DOCUMENTPO_ACTION_REVERSE_CORRECT = "com.chuboe.moeboe.po.documentpo.action.reverse_correct";
	String DOCUMENTPO_ACTION_REVERSE_ACCRUE = "com.chuboe.moeboe.po.documentpo.action.reverse_accrue";
	String DOCUMENTPO_ACTION_VOID = "com.chuboe.moeboe.po.documentpo.action.void";
}
