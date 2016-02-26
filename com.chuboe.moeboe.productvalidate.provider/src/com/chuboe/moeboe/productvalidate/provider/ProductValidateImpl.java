package com.chuboe.moeboe.productvalidate.provider;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.osgi.service.component.annotations.Component;

import com.chuboe.moeboe.product.api.ProductDTO;
import com.chuboe.moeboe.recordvalidate.api.RecordValidate;

/**
 * 
 */
@Component(name = "com.chuboe.moeboe.productvalidate")
public class ProductValidateImpl implements RecordValidate<ProductDTO> {

	@Override
	public ProductDTO validate(ProductDTO product) {
		setCoreDates(product);
		return product;
	}
	
	void setCoreDates(ProductDTO product) {
		if (product.created == 0) {
			product.created = ZonedDateTime.now(ZoneOffset.UTC).toEpochSecond();
		}
		if (product.updated == 0) {
			product.updated = ZonedDateTime.now(ZoneOffset.UTC).toEpochSecond();
		}
	}


}
