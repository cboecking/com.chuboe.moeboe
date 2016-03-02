package com.chuboe.moeboe.po.api;

import java.util.List;

import org.osgi.dto.DTO;

/**
 * @author Chuck Boecking
 * RecordDTO is the base DTO object in CRUD. 
 * It can represent almost any noun or verb (person, place, thing, action, etc...)
 * Note: there are no required fields at the DTO level. Validation will occur 
 * during save and will update the below fields accordingly.
 * A user or a location is an example of a Record. 
 */
public class RecordDTO extends DTO {
	/**
	 * Primary record ID 
	 */
	public String _id;
	
	/**
	 * Tenant that owns this record. The 'System' tenant is where all base records exists. Base records are global to all Tenants. 
	 */
	public String tenant_id;
	
	/**
	 * A Tenant can have multiple Organizations. 
	 * In CRUD, an Organization is a logical sub-tenant. 
	 * In ERP, an Organization represents a financial set of books. 
	 */
	public String organization_id;
	
	/**
	 * Name of the record. Editable by users. 
	 */
	public String name;
	
	/**
	 * Search Key is a code for the record. 
	 * Example Search Keys include 100003, CUST1003, CBOEC001. Editable by users. 
	 */
	public String searchKey;
	
	/**
	 * Date and time when record was created. 
	 */
	public long created;
	
	/**
	 * User who created the record. Will come later when user management is implemented. 
	 */
	public String createdBy_id;
	
	/**
	 * Date and time when record was last modified.
	 */
	public long updated;
	
	/**
	 * User who last updated the record. Will come later when user management is implemented.
	 */
	public String updatedBy_id;
	
	/**
	 * Dictates if the record should be used/referenced in future record creation/manipulation.
	 * There are times when a non-active record will be displayed to the user.  
	 */
	public boolean isActive = true;

	/**
	 * Dictates if the record has been deleted. 
	 * In CRUD applications, there is frequently a requirement that records cannot be permanently deleted. 
	 */
	public boolean isDeleted;
	
	/**
	 * Dictates if the record as passed all validations as of the last time the record was updated. 
	 * If the RecordValidation List is not empty, the record is not valid. 
	 */
	public boolean isRecordValid;
	
	/**
	 * Collection of validation results.  
	 */
	public List<String> recordValidation;
	
	/**
	 * If True, allows the record to act as if Valid even if RecordValidation List is not empty.
	 */
	public boolean isRecordValidBypass;
}
