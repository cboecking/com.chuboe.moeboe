package com.chuboe.moeboe.productvalidate.provider;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.osgi.service.component.annotations.Component;

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
		//TODO: be able to use the following line:
		//long tmpTime = nowEpochSecond(); // does not find the method in com.chuboe.moeboe.dateutil even though it is in the bnd build path
		long currentTime = ZonedDateTime.now(ZoneOffset.UTC).toEpochSecond();
		if (product.created == 0) {
			product.created = currentTime;
		}
		product.updated = currentTime;
	}

}
