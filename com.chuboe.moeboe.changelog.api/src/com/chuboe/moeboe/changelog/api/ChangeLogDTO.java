package com.chuboe.moeboe.changelog.api;

import java.util.List;

import org.osgi.dto.DTO;

import osgi.enroute.dto.api.DTOs.Difference;

/**
 * @author Chuck Boecking
 * Persist changes to collection records
 */
public class ChangeLogDTO extends DTO {

	/**
	 * ID of this record
	 */
	public String _id;
		
	/**
	 * ID of the record being changed 
	 */
	public String collection_id;
	
	/**
	 * Collection of changes from OLD to NEW 
	 */
	public List<Difference> changes;
	
	/**
	 * Name of the collection being changed 
	 */
	public String collection;
	
	/**
	 * User ID of the person making the changes
	 */
	public String createdBy_id; 

}
