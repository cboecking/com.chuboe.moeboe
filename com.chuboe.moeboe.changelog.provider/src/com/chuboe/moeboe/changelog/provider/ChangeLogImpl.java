package com.chuboe.moeboe.changelog.provider;

import java.util.List;

import org.osgi.dto.DTO;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;

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
	
	@Override
	public void handleEvent(Event event) {

		//System.out.println("Change Log Here - Represent!!!");
		
		//TODO: initialize storage
		
		//find difference between new and old
		DTO newDTO = (DTO) event.getProperty(RecordPO.RECORDPO_EVENT_PROPERTY_DTO_NEW);
		DTO oldDTO = (DTO) event.getProperty(RecordPO.RECORDPO_EVENT_PROPERTY_DTO_OLD);
		try {
			List<Difference> diffs = dtos.diff(oldDTO, newDTO);
			for(Difference diff: diffs) {
				System.out.println(diff);
				Retrieve retrieveNew = dtos.get(newDTO, diff.path);
				Retrieve retrieveOld = dtos.get(oldDTO, diff.path);
				System.out.println("New: " + retrieveNew.toString());
				System.out.println("Old: " + retrieveOld.toString());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//TODO: persist differences
		
	}

}
