package com.chuboe.moeboe.product.provider;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.log.LogService;

import com.chuboe.moeboe.po.api.RecordPO;
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
	LogService log;
	
	//TEST - can we abstract base logic to a generic class/service - shared between all CRUD entities?
	@Reference
	RecordPO<ProductDTO> po;
	
	@Activate
	void activate() throws Exception {
		if(count("none")==0)
		{
			ProductDTO p = new ProductDTO();
			p.name = "First Product";
			List<String> valList = new ArrayList<>();
			valList.add("First Validation Entry");
			p.recordValidation = valList;
			p.isActive=false;
			p.isRecordValid=true;
			p = save(p);
			
			//KP: logging with a lambda expression from a MongoDB stream
			po.getStore(ProductDTO.class, ProductDTO.class.getSimpleName()).all().stream()
				.forEach(lam -> log.log(LogService.LOG_INFO, "Activate Product -> Create first entry: _id = " +lam._id+ "; Name = " + lam.name));
			//to view logs: enter "help log" in the console (assuming gogo-shell and gogo command are installed)
			
			po.getStore(ProductDTO.class, ProductDTO.class.getSimpleName()).all().stream()
				.forEach(lam -> System.out.println(lam._id + "::" + lam.name));
		}
	} //activate
	
	@Override
	public ProductDTO save(ProductDTO product) throws Exception {
		return po.save(ProductDTO.class, ProductDTO.class.getSimpleName(), product);
	}

	@Override
	public ProductDTO find(String _id) throws Exception {
		return po.find(ProductDTO.class, ProductDTO.class.getSimpleName(), _id);
	}

	@Override
	public List<ProductDTO> list(String filter) throws Exception {
		return po.list(ProductDTO.class, ProductDTO.class.getSimpleName(), filter);
	}

	@Override
	public boolean delete(String _id) throws Exception {
		return po.delete(ProductDTO.class, ProductDTO.class.getSimpleName(), _id);
	}

	@Override
	public int count(String filter) throws Exception {
		return po.count(ProductDTO.class, ProductDTO.class.getSimpleName(), filter);
	}

}
