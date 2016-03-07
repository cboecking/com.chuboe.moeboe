package com.chuboe.moeboe.po.api;

import java.util.List;

/**
 * 
 */
public interface RecordPO<T extends RecordDTO> {
	
	/**
	 * 
	 */
	T save(Class<T> clazz, String collection, T t) throws Exception;
	T find(Class<T> clazz, String collection, String _id) throws Exception;
	List<T> list(Class<T> clazz, String collection, String filter) throws Exception;
	boolean delete(Class<T> clazz, String collection, String _id) throws Exception;
	int count(Class<T> clazz, String collection, String filter) throws Exception;
	
	String RECORDPO_ACTION_ALL = "com/chuboe/moeboe/po/recordpo/action/*";
	String RECORDPO_ACTION_SAVE = "com/chuboe/moeboe/po/recordpo/action/save";
	String RECORDPO_ACTION_DELETE = "com/chuboe/moeboe/po/recordpo/action/delete";
	
}
