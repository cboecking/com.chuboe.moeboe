package com.chuboe.moeboe.po.api;

import java.util.List;

import aQute.open.store.api.Store;

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
	
}
