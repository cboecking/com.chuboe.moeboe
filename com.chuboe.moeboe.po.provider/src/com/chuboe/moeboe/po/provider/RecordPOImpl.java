package com.chuboe.moeboe.po.provider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.ServiceTracker;

import com.chuboe.moeboe.po.api.RecordPO;
import com.chuboe.moeboe.po.api.RecordDTO;
import com.chuboe.moeboe.recordvalidate.api.RecordValidate;

import aQute.open.store.api.DB;
import aQute.open.store.api.Store;
import osgi.enroute.dto.api.DTOs;

/**
 * @param <T>
 * 
 */
@Component(
		name = "com.chuboe.moeboe.po",
		property = {
				"POType=DocumentDB" 
		}
	)
public class RecordPOImpl<T extends RecordDTO> implements RecordPO<T> {
	
	@Reference
	DB db;
	
	@Reference
	LogService log;
	
	@Reference
	EventAdmin eventAdmin;
	
	@Reference
	DTOs dtos;
	
	//begin -- maintain list of validators
	Map<String, List<RecordValidate<T>>> validatorMap = new ConcurrentHashMap<>();
	
	//KP: (key point) reference or call on multiple services synchronously or in-line
	//KP: How to get component configuration properties - add "Map<String,Object> config" as a parameter  
	//KP: Whiteboard example - before save logic
	@Reference(
			cardinality=ReferenceCardinality.MULTIPLE,
			policy=ReferencePolicy.DYNAMIC
		)
	void addRecordValidate(RecordValidate<T> rv, Map<String,Object> config) {
		String validateType = (String) config.get(RecordValidate.RECORD_VALIDATE_CONFIG_TYPE);
		if(validatorMap.containsKey(validateType)){
			validatorMap.get(validateType).add(rv);
		}
		else{
			List<RecordValidate<T>> validatorList = new CopyOnWriteArrayList<>();
			validatorList.add(rv);
			validatorMap.put(validateType, validatorList);
		}
	}
	
	void removeRecordValidate(RecordValidate<T> rv, Map<String,Object> config) {
		String validateType = (String) config.get(RecordValidate.RECORD_VALIDATE_CONFIG_TYPE);
		if(validatorMap.containsKey(validateType)){
			validatorMap.get(validateType).remove(rv);
			//check to see if the map entry is now empty - if so, remove it
			if(validatorMap.get(validateType).isEmpty())
				validatorMap.remove(validateType);
		}
	}
	//end -- maintain list of validators
	
	@Override
	public T save(Class<T> clazz, String collectionName, T t) throws Exception {
		Store<T> store = db.getStore(clazz, collectionName);
		
		log.log(LogService.LOG_DEBUG, "Entering RecordPO.save: "+t);
		
		clearValidationFields(t);
		
		//TODO: find record base validators - the ones that apply to all services
		//TODO: code here
		
		log.log(LogService.LOG_DEBUG, "RecordPO.save after record base validators: "+t);
		
		//TODO; find service specific validators - see above validators notes about limiting...
		List<RecordValidate<T>> validatorList = validatorMap.get(collectionName);
		if(validatorList != null) {
			for(RecordValidate<T> rv: validatorList) {
				log.log(LogService.LOG_DEBUG, "RecordPO.save calling validate on: "+rv.getClass().getName());
				rv.validate(t, RECORDPO_ACTION_SAVE);
			}
		}
		
		log.log(LogService.LOG_DEBUG, "RecordPO.save after record validators: "+t);
		
		setValidationFields(t);
		
		//find old version for change log comparison
		T t_old;
		if(t._id != null) {
			t_old = find(clazz, collectionName, t._id);
		} else {
			t_old = null;
		}

		t = store.insert(t);
		
		log.log(LogService.LOG_DEBUG, "RecordPO.save after insert: "+t);
		
		//begin - post save events 

		postActionEvent(RECORDPO_ACTION_SAVE, collectionName, t);
		postChangeLogEvent(RECORDPO_CHANGE_LOG, collectionName, t, t_old);
		
		//end - post save events
		
		log.log(LogService.LOG_DEBUG, "Entering RecordPO.save: "+t);
		
		return t;
	}

	@Override
	public T find(Class<T> clazz, String collectionName, String _id) throws Exception {
		Store<T> store = db.getStore(clazz, collectionName);
		if(store.find("_id="+_id).one().isPresent())
			return store.find("_id="+_id).one().get();
		return null;
	}

	@Override
	public List<T> list(Class<T> clazz, String collectionName, String filter) throws Exception {
		Store<T> store = db.getStore(clazz, collectionName);
		return store.find((isFilterEmpty(filter)) ? "_id=*" : filter).collect();
	}

	@Override
	public boolean delete(Class<T> clazz, String collectionName, String _id) throws Exception {
		Store<T> store = db.getStore(clazz, collectionName);
		
		//TODO: need to store.find the record, clear validations, validate the delete action, act accordingly
		//TODO: use configuration to determine if record can be deleted or just flagged as deleted
		
		int count = store.find("_id="+_id).remove();
		//TODO return an Response Object instead
		return (count>0)?true:false;
	}

	@Override
	public int count(Class<T> clazz, String collectionName, String filter) throws Exception {
		Store<T> store = db.getStore(clazz, collectionName);
		return store.find((isFilterEmpty(filter)) ? "_id=*" : filter).count();
	}
	
	boolean isFilterEmpty(String filter) {
		return filter == null || filter.isEmpty() || filter.equals("none");
	}
	
	void clearValidationFields(T t) {
		t.isRecordValid=true;
		t.recordValidation.clear();		
	}
	
	void setValidationFields(T t) {
		if(!t.recordValidation.isEmpty())
			t.isRecordValid=false;
	}
	
	protected void postActionEvent(String event, String collectionName, T t) {
		//KP: using Event Admin to fire an event - do not care if anyone is listening - asynchronous processing
		Map<String, Object> actionProperties = new HashMap<>();

		actionProperties.put(RECORDPO_EVENT_PROPERTY_COLLECTION, collectionName);
		actionProperties.put(RECORDPO_EVENT_PROPERTY_DTO_NEW, t);
		Event actionEvent = new Event(event+"/"+collectionName, actionProperties);
		eventAdmin.postEvent(actionEvent);
	}

	protected void postChangeLogEvent(String event, String collectionName, T t, T t_old) {
		Map<String, Object> changeLogProperties = new HashMap<>();

		//TODO: check configuration to see if the collection wants a change log
		changeLogProperties.put(RECORDPO_EVENT_PROPERTY_COLLECTION, collectionName);
		changeLogProperties.put(RECORDPO_EVENT_PROPERTY_DTO_OLD, t_old);
		changeLogProperties.put(RECORDPO_EVENT_PROPERTY_DTO_NEW, t);
		Event changeLogEvent = new Event(event, changeLogProperties);
		eventAdmin.postEvent(changeLogEvent);
	}
	
	//TODO: consider creating a dynamic service lookup that find services for filters passed into the above methods
//	private final BundleContext context = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
//	
//	protected <T> T getService(Class<T> clazz) throws InterruptedException {
//		ServiceTracker<T,T> st = new ServiceTracker<>(context, clazz, null);
//		st.open();
//		return st.waitForService(1000);
//	} 
	
}
