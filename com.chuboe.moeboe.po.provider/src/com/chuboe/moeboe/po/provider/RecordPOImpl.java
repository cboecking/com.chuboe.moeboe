package com.chuboe.moeboe.po.provider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
	
	//begin -- list of validators
	//TODO: Changeme: this should be a Map of Lists - each map key should be type of validator (Ex. ProductDTO)
	//TODO: Changeme: this currently executes all validators regardless of the validator type. 
	List<RecordValidate<T>> validators = new CopyOnWriteArrayList<>();
	
	//KP: (key point) reference or call on multiple services synchronously or in-line
	//KP: Whiteboard example - before save logic
	@Reference(
			cardinality=ReferenceCardinality.MULTIPLE,
			policy=ReferencePolicy.DYNAMIC
		)
	void addRecordValidate(RecordValidate<T> rv) {
		//TODO: need to ask Peter how to get the validator's config/target so build
		validators.add(rv);
	}
	
	void removeRecordValidate(RecordValidate<T> rv) {
		validators.remove(rv);
	}
	//end -- list of validators
	
	@Override
	public T save(Class<T> clazz, String collection, T t) throws Exception {
		Store<T> store = db.getStore(clazz, collection);
		
		log.log(LogService.LOG_DEBUG, "Entering RecordPO.save: "+t);
		
		clearValidationFields(t);
		
		//TODO: find record base validators - the ones that apply to all services
		//code here
		
		log.log(LogService.LOG_DEBUG, "RecordPO.save after record base validators: "+t);
		
		//TODO; find service specific validators - see above validators notes about limiting...
		for(RecordValidate<T> rv: validators) {
			rv.validate(t, RECORDPO_ACTION_SAVE);
		}
		
		log.log(LogService.LOG_DEBUG, "RecordPO.save after record validators: "+t);
		
		setValidationFields(t);
		
		//find old version for change log comparison
		T t_old;
		if(t._id != null) {
			t_old = find(clazz, collection, t._id);
		} else {
			t_old = null;
		}

		t = store.insert(t);
		
		log.log(LogService.LOG_DEBUG, "RecordPO.save after insert: "+t);
		
		//begin - post save events 

		postActionEvent(RECORDPO_ACTION_SAVE, collection, t);
		postChangeLogEvent(RECORDPO_CHANGE_LOG, collection, t, t_old);
		
		//end - post save events
		
		return t;
	}

	@Override
	public T find(Class<T> clazz, String collection, String _id) throws Exception {
		Store<T> store = db.getStore(clazz, collection);
		if(store.find("_id="+_id).one().isPresent())
			return store.find("_id="+_id).one().get();
		return null;
	}

	@Override
	public List<T> list(Class<T> clazz, String collection, String filter) throws Exception {
		Store<T> store = db.getStore(clazz, collection);
		return store.find((isFilterEmpty(filter)) ? "_id=*" : filter).collect();
	}

	@Override
	public boolean delete(Class<T> clazz, String collection, String _id) throws Exception {
		Store<T> store = db.getStore(clazz, collection);
		
		//TODO: need to store.find the record, clear validations, validate the delete action, act accordingly
		//TODO: use configuration to determine if record can be deleted or just flagged as deleted
		
		int count = store.find("_id="+_id).remove();
		//TODO return an Response Object instead
		return (count>0)?true:false;
	}

	@Override
	public int count(Class<T> clazz, String collection, String filter) throws Exception {
		Store<T> store = db.getStore(clazz, collection);
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
	
	protected void postActionEvent(String event, String collection, T t) {
		//KP: using Event Admin to fire an event - do not care if anyone is listening - asynchronous processing
		Map<String, Object> actionProperties = new HashMap<>();

		actionProperties.put(RECORDPO_EVENT_PROPERTY_COLLECTION, collection);
		actionProperties.put(RECORDPO_EVENT_PROPERTY_DTO_NEW, t);
		Event actionEvent = new Event(event+"/"+collection, actionProperties);
		eventAdmin.postEvent(actionEvent);
	}

	protected void postChangeLogEvent(String event, String collection, T t, T t_old) {
		Map<String, Object> changeLogProperties = new HashMap<>();

		//TODO: check configuration to see if the collection wants a change log
		changeLogProperties.put(RECORDPO_EVENT_PROPERTY_COLLECTION, collection);
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
