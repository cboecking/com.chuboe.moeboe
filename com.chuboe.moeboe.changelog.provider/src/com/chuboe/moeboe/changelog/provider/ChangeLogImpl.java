package com.chuboe.moeboe.changelog.provider;

import java.util.ArrayList;
import java.util.List;

import org.osgi.dto.DTO;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.osgi.service.log.LogService;

import com.chuboe.moeboe.changelog.api.ChangeLog;
import com.chuboe.moeboe.changelog.api.ChangeLog.ChangeLogDetails;
import com.chuboe.moeboe.changelog.api.ChangeLogDTO;
import com.chuboe.moeboe.po.api.RecordDTO;
import com.chuboe.moeboe.po.api.RecordPO;

import aQute.open.store.api.DB;
import aQute.open.store.api.Store;
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
	DB db;

	@Reference
	DTOs dtos;
	
	@Reference
	LogService log;
	
	@Override
	public void handleEvent(Event event) {

		//System.out.println("Change Log Here - Represent!!!");
		
		try {
			Store<ChangeLogDTO> store = db.getStore(ChangeLogDTO.class, ChangeLogDTO.class.getSimpleName());
			
			//find difference between new and old
			RecordDTO newDTO = (RecordDTO) event.getProperty(RecordPO.RECORDPO_EVENT_PROPERTY_DTO_NEW);
			RecordDTO oldDTO = (RecordDTO) event.getProperty(RecordPO.RECORDPO_EVENT_PROPERTY_DTO_OLD);

			List<Difference> diffs = dtos.diff(oldDTO, newDTO);
			
			ChangeLogDTO cl = new ChangeLogDTO();
			cl.collection = (String) event.getProperty(RecordPO.RECORDPO_EVENT_PROPERTY_COLLECTION);
			log.log(LogService.LOG_INFO, "Change Log Collection Name: " + cl.collection);
			cl.collection_id = newDTO._id;
			cl.changes = new ArrayList<>();
			
			for(Difference diff: diffs) {

				//System.out.println(diff);
				log.log(LogService.LOG_INFO, "Change Log Difference: " + diff);
				
				Retrieve retrieveNew = dtos.get(newDTO, diff.path);
				Retrieve retrieveOld = dtos.get(oldDTO, diff.path);
				
				ChangeLogDetails cld = new ChangeLogDetails();
				cld.before = retrieveOld.toString();
				cld.after = retrieveNew.toString();
				cl.changes.add(cld);

				//System.out.println("Old: " + retrieveOld.toString());
				log.log(LogService.LOG_INFO, "Old: " + retrieveOld);
				//System.out.println("New: " + retrieveNew.toString());
				log.log(LogService.LOG_INFO, "New: " + retrieveNew);
			}
			
			store.insert(cl);
			
		} catch (Exception e) {
			//e.printStackTrace();
			log.log(LogService.LOG_ERROR, "ChangeLogImpl Exception during diff", e);
		}
		
		//TODO: persist differences
		
	}

}
