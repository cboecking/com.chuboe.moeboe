package com.chuboe.moeboe.po.provider;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.log.LogService;

import com.chuboe.moeboe.po.api.PO;
import com.chuboe.moeboe.po.api.RecordDTO;

import aQute.open.store.api.DB;
import aQute.open.store.api.Store;

/**
 * @param <T>
 * 
 */
@Component(name = "com.chuboe.moeboe.po")
public class POImpl<T extends RecordDTO> implements PO<T> {
	
	@Reference
	DB db;
	
	@Reference
	LogService log;
	
	@Override
	public T save(Class<T> clazz, String collection, T t) throws Exception {
		Store<T> store = db.getStore(clazz, collection);
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
		return store.find((filter == null || filter.isEmpty() || filter.equals("none")) ? "_id=*" : filter).collect();
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
		return store.find((filter == null || filter.isEmpty() || filter.equals("none")) ? "_id=*" : filter).count();
	}

	@Override
	public Store<T> getStore(Class<T> clazz, String collection) throws Exception {
		return db.getStore(clazz, collection);
	}

}
