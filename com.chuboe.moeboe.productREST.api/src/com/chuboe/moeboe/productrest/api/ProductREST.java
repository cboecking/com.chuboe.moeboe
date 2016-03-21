package com.chuboe.moeboe.productrest.api;

import java.util.List;

import com.chuboe.moeboe.product.api.ProductDTO;

import osgi.enroute.rest.api.RESTRequest;

/**
 * 
 */
public interface ProductREST {
	
	/**
	 * 
	 */
	ProductDTO getProduct(String _id) throws Exception;
	List<ProductDTO> getProducts(String filter) throws Exception;

	interface ProductRequest extends RESTRequest {
		ProductDTO _body();
	}
	ProductDTO postProduct(ProductDTO product) throws Exception;

	boolean deleteProduct(String _id) throws Exception;

}
