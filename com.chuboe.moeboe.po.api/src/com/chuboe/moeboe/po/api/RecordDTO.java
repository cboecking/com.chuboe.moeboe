package com.chuboe.moeboe.po.api;

import java.util.List;

import org.osgi.dto.DTO;

public class RecordDTO extends DTO {
	public String _id;
	public String name;
	public String searchKey;
	public long created;
	//public String createdBy_id; - will come later
	public long updated;
	//public String updatedBy_id; - will come later
	public boolean isActive = true;
	public boolean isDeleted;
	public boolean isRecordValid;
	public List<String> recordValidation;
}
