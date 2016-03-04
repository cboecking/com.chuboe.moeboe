package com.chuboe.moeboe.productaction.provider;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;
import org.osgi.service.log.LogService;

import com.chuboe.moeboe.po.api.RecordPO;
import com.chuboe.moeboe.product.api.ProductDTO;

/**
 * 
 */
//@Component(name = "com.chuboe.moeboe.productaction")
@Component
public class ProductActionImpl implements EventHandler {
	
	@Reference
	LogService log;

	@Override
	public void handleEvent(Event event) {
		
		System.out.println("I AM HERE...");
		
		if(event.getTopic().equals(RecordPO.RECORDPO_ACTION_SAVE+"/"+ProductDTO.class.getSimpleName())) {
			//do after save stuff - always in separate protected methods
			log.log(LogService.LOG_INFO, "Product Event Handler for: "+ RecordPO.RECORDPO_ACTION_SAVE+"/"+ProductDTO.class.getSimpleName());
			
		}
		if(event.getTopic().equals(RecordPO.RECORDPO_ACTION_DELETE+"/"+ProductDTO.class.getSimpleName())) {
			//do after delete stuff - always in separate protected methods
			log.log(LogService.LOG_INFO, "Product Event Handler for: "+ RecordPO.RECORDPO_ACTION_DELETE+"/"+ProductDTO.class.getSimpleName());
		}
		
	}

}
