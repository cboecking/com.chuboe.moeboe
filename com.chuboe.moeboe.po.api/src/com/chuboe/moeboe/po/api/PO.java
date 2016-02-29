package com.chuboe.moeboe.po.api;

import java.util.List;

/**
 * 
 */
public interface PO<T> {
	
	/**
	 * 
	 */
	T save(T t) throws Exception;
	T find(String _id) throws Exception;
	List<T> list(String filter) throws Exception;
	boolean delete(String _id) throws Exception;
	
}
