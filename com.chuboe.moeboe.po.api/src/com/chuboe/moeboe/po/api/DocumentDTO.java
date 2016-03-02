package com.chuboe.moeboe.po.api;

/**
 * @author Chuck Boecking
 * DocumentDTO is an extension of RecordDTO. It adds transactional behavior to a Record.
 * A Sales Order is an example of a Document. It is transactional, and it manages other Records (Sales Order Line Records) 
 * 
 */
public class DocumentDTO extends RecordDTO {
	
	/**
	 * Document Type describes the record's workflow and abilities 
	 */
	public String documentType_id;

	/**
	 * Document Status describes the record's status in its document workflow
	 */
	public String documentStatus_id;
	
	/**
	 * Orientation describes the records direction such as Sales vs. Purchase, In vs. Out, etc... 
	 */
	public String orientation_id;

}