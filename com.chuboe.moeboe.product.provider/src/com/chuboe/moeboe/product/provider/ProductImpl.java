package com.chuboe.moeboe.product.provider;

import java.util.ArrayList;
import java.util.List;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.log.LogService;

import com.chuboe.moeboe.po.api.RecordPO;
import com.chuboe.moeboe.product.api.Product;
import com.chuboe.moeboe.product.api.ProductDTO;

/**
 * 
 */
@Component(name = "com.chuboe.moeboe.product")
public class ProductImpl implements Product {

	@Reference
	LogService log;
	
	@Reference
	RecordPO<ProductDTO> po;
	//NOTE: In the future, property = {"POType=DocumentDB"} can be used to specify what type of storage you choose (DocumentDB vs RelationalDB)
	
	@Activate
	void activate() throws Exception {
		if(count("none")==0)
		{
			ProductDTO p = new ProductDTO();
			p.name = "First Product";
			//p.recordValidation.add("First Validation Entry");
			//p.isActive=false;
			//p.isRecordValid=true;
			p = save(p);
			
			list("none").stream()
				.forEach(lam -> log.log(LogService.LOG_INFO, "Activate Product -> Create first entry: _id = " +lam._id+ "; Name = " + lam.name));
			//to view logs: enter "help log" in the console (assuming gogo-shell and gogo command are installed)
			
			list("none").stream()
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
