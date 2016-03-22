package com.chuboe.moeboe.po.api;

import java.util.List;

/**
 * 
 */
public interface RecordPO<T extends RecordDTO> {
	
	/**
	 * 
	 */
	T save(Class<T> clazz, String collectionName, T t) throws Exception;
	T find(Class<T> clazz, String collectionName, String _id) throws Exception;
	List<T> list(Class<T> clazz, String collectionName, String filter) throws Exception;
	boolean delete(Class<T> clazz, String collectionName, String _id) throws Exception;
	int count(Class<T> clazz, String collectionName, String filter) throws Exception;
	
	String RECORDPO_ACTION_ALL = "com/chuboe/moeboe/po/recordpo/action/*";
	String RECORDPO_ACTION_SAVE = "com/chuboe/moeboe/po/recordpo/action/save";
	String RECORDPO_ACTION_DELETE = "com/chuboe/moeboe/po/recordpo/action/delete";
	
	//TODO: change to enum
	String RECORDPO_EVENT_PROPERTY_DTO_OLD = "DTO_OLD";
	String RECORDPO_EVENT_PROPERTY_DTO_NEW = "DTO_NEW";
	String RECORDPO_EVENT_PROPERTY_COLLECTION = "COLLECTION";
	
	String RECORDPO_CHANGE_LOG = "com/chuboe/moeboe/po/recordpo/change/log";
	
}
