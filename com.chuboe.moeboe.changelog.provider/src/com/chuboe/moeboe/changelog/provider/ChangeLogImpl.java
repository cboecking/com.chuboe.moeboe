package com.chuboe.moeboe.changelog.provider;

import java.util.List;

import org.osgi.dto.DTO;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.osgi.service.log.LogService;

import com.chuboe.moeboe.changelog.api.ChangeLog;
import com.chuboe.moeboe.po.api.RecordPO;

import osgi.enroute.dto.api.DTOs;
import osgi.enroute.dto.api.DTOs.Difference;
import osgi.enroute.dto.api.DTOs.Retrieve;

/**
 * 
 */
@Component(
		name = "com.chuboe.moeboe.changelog",
		property=EventConstants.EVENT_TOPIC+"="+RecordPO.RECORDPO_CHANGE_LOG
	)
public class ChangeLogImpl implements EventHandler {

	@Reference
	DTOs dtos;
	
	@Reference
	LogService log;
	
	@Override
	public void handleEvent(Event event) {

		//TODO: change below System.out to log statements
		//System.out.println("Change Log Here - Represent!!!");
		
		//TODO: initialize storage
		
		//find difference between new and old
		DTO newDTO = (DTO) event.getProperty(RecordPO.RECORDPO_EVENT_PROPERTY_DTO_NEW);
		DTO oldDTO = (DTO) event.getProperty(RecordPO.RECORDPO_EVENT_PROPERTY_DTO_OLD);
		try {
			List<Difference> diffs = dtos.diff(oldDTO, newDTO);
			for(Difference diff: diffs) {

				//System.out.println(diff);
				log.log(LogService.LOG_INFO, "Change Log Difference: " + diff);
				
				Retrieve retrieveNew = dtos.get(newDTO, diff.path);
				Retrieve retrieveOld = dtos.get(oldDTO, diff.path);

				//System.out.println("Old: " + retrieveOld.toString());
				log.log(LogService.LOG_INFO, "Old: " + retrieveOld);
				//System.out.println("New: " + retrieveNew.toString());
				log.log(LogService.LOG_INFO, "New: " + retrieveNew);
			}
		} catch (Exception e) {
			//e.printStackTrace();
			log.log(LogService.LOG_ERROR, "ChangeLogImpl Exception during diff", e);
		}
		
		//TODO: persist differences
		
	}

}
