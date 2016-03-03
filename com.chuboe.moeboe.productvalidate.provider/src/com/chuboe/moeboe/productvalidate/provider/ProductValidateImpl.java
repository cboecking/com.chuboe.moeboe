package com.chuboe.moeboe.productvalidate.provider;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.osgi.service.component.annotations.Component;

import com.chuboe.moeboe.dateutil.api.DateUtil;
import com.chuboe.moeboe.po.api.RecordPO;
import com.chuboe.moeboe.product.api.ProductDTO;
import com.chuboe.moeboe.recordvalidate.api.RecordValidate;

/**
 * 
 */
@Component(
		name = "com.chuboe.moeboe.productvalidate",
		property = {
				"validateDTO=ProductDTO" 
		}
	)
public class ProductValidateImpl implements RecordValidate<ProductDTO> {

	//KP: use of a library (as opposed to a service component). Libraries have no observable state.
	DateUtil du;
	
	@Override
	public ProductDTO validate(ProductDTO product, String recordPOAction) {
		if(recordPOAction.equals(RecordPO.RECORDPO_ACTION_SAVE)) {
			setCoreDates(product);
		}
		else if(recordPOAction.equals(RecordPO.RECORDPO_ACTION_DELETE)) {
			// do nothing yet
		}
		else {
			product.recordValidation.add("No Action found!!");
		}
		
		return product;
	}

	void setCoreDates(ProductDTO product) {
		
		//This is the non-desirable way
		long currentTime = ZonedDateTime.now(ZoneOffset.UTC).toEpochSecond();
		
		//This line prevents the services from loading
		//currentTime = du.nowEpochSecond();
		
		if (product.created == 0) {
			product.created = currentTime;
		}
		product.updated = currentTime;
	}

}
