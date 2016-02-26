package com.chuboe.moeboe.product.provider;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.log.LogService;

import com.chuboe.moeboe.product.api.Product;
import com.chuboe.moeboe.product.api.ProductDTO;
import com.chuboe.moeboe.recordvalidate.api.RecordValidate;

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
	
	//list of product validators
	List<RecordValidate<ProductDTO>> validators = new CopyOnWriteArrayList<>();
	
	@Reference(
			cardinality=ReferenceCardinality.MULTIPLE,
			policy=ReferencePolicy.DYNAMIC
		)
	void addRecordValidate(RecordValidate<ProductDTO> rv) {
		validators.add(rv);
	}
	
	void removeRecordValidate(RecordValidate<ProductDTO> rv) {
		validators.remove(rv);
	}
	//end -- list of product validators
	

	@Activate
	void activate() throws Exception {
		Store<ProductDTO> store = db.getStore(ProductDTO.class, "product");
		if(store.count() == 0)
		{
			ProductDTO p = new ProductDTO();
			p.name = "First Product";
			p = save(p);
			
			//KP: logging with a lambda expression from a MongoDB stream
			store.all().stream()
				.forEach(lam -> log.log(LogService.LOG_INFO, "Activate Product -> Create first entry: _id = " +lam._id+ "; Name = " + lam.name));
			//to view logs: enter "help log" in the console (assuming gogo-shell and gogo command are installed)
			
			store.all().stream()
				.forEach(lam -> System.out.println(lam._id + "::" + lam.name));
		}
	} //activate
	
	@Override
	public ProductDTO save(ProductDTO product) throws Exception {
		Store<ProductDTO> store = db.getStore(ProductDTO.class, "product");
		
		for(RecordValidate<ProductDTO> rv: validators) {
			rv.validate(product);
		}
		
		return store.insert(product);
	}

	@Override
	public ProductDTO find(String _id) throws Exception {
		Store<ProductDTO> store = db.getStore(ProductDTO.class, "product");
		//TODO ask Peter about how to handle when no result found - .get() throws NoSuchElementException 
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
