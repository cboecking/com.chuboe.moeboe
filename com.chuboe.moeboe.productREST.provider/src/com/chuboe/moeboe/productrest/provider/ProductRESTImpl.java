package com.chuboe.moeboe.productrest.provider;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.chuboe.moeboe.product.api.Product;
import com.chuboe.moeboe.product.api.ProductDTO;
import com.chuboe.moeboe.productrest.api.ProductREST;

import osgi.enroute.configurer.api.RequireConfigurerExtender;
import osgi.enroute.rest.api.REST;
import osgi.enroute.webserver.capabilities.RequireWebServerExtender;

/**
 * 
 */
@RequireWebServerExtender
@RequireConfigurerExtender
@Component(name = "com.chuboe.moeboe.productREST")
public class ProductRESTImpl implements ProductREST, REST {

	@Reference
	Product product;
	
	@Override
	public ProductDTO getProduct(String _id) throws Exception {
		return product.find(_id);
	}

	@Override
	public List<ProductDTO> getProducts(String filter) throws Exception {
		return product.list(filter);
	}

	@Override
	public ProductDTO postProduct(ProductDTO product) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deleteProduct(String _id) throws Exception {
		return product.delete(_id);
	}

}