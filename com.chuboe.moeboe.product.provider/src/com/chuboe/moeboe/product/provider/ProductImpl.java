package com.chuboe.moeboe.product.provider;

import java.util.List;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.log.LogService;

import com.chuboe.moeboe.product.api.Product;
import com.chuboe.moeboe.product.api.ProductDTO;

import aQute.open.store.api.DB;
import aQute.open.store.api.Store;

/**
 * 
 */
@Component(name = "com.chuboe.moeboe.product")
public class ProductImpl implements Product {

	@Reference
	DB db;
	
	@Reference
	LogService log;

	@Activate
	void activate() throws Exception {
		Store<ProductDTO> store = db.getStore(ProductDTO.class, "product");
		if(store.count() == 0)
		{
			ProductDTO p = new ProductDTO();
			p.name = "First Product";
			p = store.insert(p);
			
			//KP: logging with a lambda expression from a MongoDB stream
			store.all().stream()
				.forEach(lam -> log.log(LogService.LOG_INFO, "Activate Product -> Create first entry: _id = " +lam._id+ "; Name = " + lam.name));
			//to view logs: enter "help log" in the console (assuming gogo-shell and gogo command are installed)
			
			store.all().stream()
				.forEach(lam -> System.out.println(lam._id + "::" + lam.name));
		}
	}
	
	@Override
	public ProductDTO save(ProductDTO product) throws Exception {
		Store<ProductDTO> store = db.getStore(ProductDTO.class, "product");
		return store.insert(product);
	}

	@Override
	public ProductDTO find(String _id) throws Exception {
		Store<ProductDTO> store = db.getStore(ProductDTO.class, "product");
		return store.find("_id="+_id).one().get();
	}

	@Override
	public List<ProductDTO> list(String filter) throws Exception {
		Store<ProductDTO> store = db.getStore(ProductDTO.class, "product");
		return store.find((filter == null || filter.isEmpty() || filter.equals("none")) ? "_id=*" : filter).collect();
	}

	@Override
	public boolean delete(String _id) throws Exception {
		Store<ProductDTO> store = db.getStore(ProductDTO.class, "product");
		int count = store.find("_id="+_id).remove();
		//TODO return an Response Object instead
		return (count>0)?true:false;
	}

}
