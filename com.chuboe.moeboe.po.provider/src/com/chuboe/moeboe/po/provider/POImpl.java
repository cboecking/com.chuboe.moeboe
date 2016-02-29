package com.chuboe.moeboe.po.provider;

import java.util.List;

import org.osgi.service.component.annotations.Component;

import com.chuboe.moeboe.po.api.PO;

/**
 * @param <T>
 * 
 */
@Component(name = "com.chuboe.moeboe.po")
public class POImpl<T> implements PO<T> {
	
//	@Reference
//	DB db;
	
	@Override
	public T save(T t) throws Exception {
		//this does not work - cannot call T.class
		//Store<T> store = db.getStore(T.class, t.getClass().getSimpleName());
		System.out.println("Save" + t.toString());
		return t;
	}

	@Override
	public T find(String _id) throws Exception {
		System.out.println("Find");
		return null;
	}

	@Override
	public List<T> list(String filter) throws Exception {
		System.out.println("List");
		return null;
	}

	@Override
	public boolean delete(String _id) throws Exception {
		System.out.println("Delete");
		return true;
	}

}
