package com.chuboe.moeboe.po.provider;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.ServiceTracker;

import com.chuboe.moeboe.po.api.RecordPO;
import com.chuboe.moeboe.po.api.RecordDTO;
import com.chuboe.moeboe.recordvalidate.api.RecordValidate;

import aQute.open.store.api.DB;
import aQute.open.store.api.Store;

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
	
	//begin -- list of validators
	//TODO: Changeme: this should be a Map of Lists - each map entry should be type of validator
	//TODO: Changeme: this currently executes all validators regardless of the validator type. 
	List<RecordValidate<T>> validators = new CopyOnWriteArrayList<>();
	
	//KP: (key point) reference or call on multiple services
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
		
		//TODO: find base validators - applies to all services
		//code here
		
		//TODO; find service specific validators
		for(RecordValidate<T> rv: validators) {
			rv.validate(t);
		}
		return store.insert(t);
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
	
	//TODO: consider creating a dynamic service lookup that find services for filters passed into the above methods
//	private final BundleContext context = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
//	
//	protected <T> T getService(Class<T> clazz) throws InterruptedException {
//		ServiceTracker<T,T> st = new ServiceTracker<>(context, clazz, null);
//		st.open();
//		return st.waitForService(1000);
//	} 
	
}
