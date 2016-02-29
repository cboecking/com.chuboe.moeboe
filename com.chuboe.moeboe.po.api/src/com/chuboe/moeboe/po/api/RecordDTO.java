package com.chuboe.moeboe.po.api;

import org.osgi.dto.DTO;

public class RecordDTO extends DTO {
	public String _id;
	public String name;
	public long created;
	public long updated;
	public boolean isActive;
	public boolean isDeleted;
}
