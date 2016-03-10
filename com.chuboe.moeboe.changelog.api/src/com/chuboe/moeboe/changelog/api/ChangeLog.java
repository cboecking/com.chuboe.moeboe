package com.chuboe.moeboe.changelog.api;

import org.osgi.dto.DTO;
import org.osgi.service.event.EventHandler;

/**
 * 
 */
public interface ChangeLog extends EventHandler{
	
	/**
	 * 
	 */
	
	public class ChangeLogDetails extends DTO {
		public String before;
		public String after;
	}
	
}
