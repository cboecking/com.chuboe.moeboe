package com.chuboe.moeboe.product.api;

import java.util.List;

/**
 * 
 */
public interface Product {
	
	/**
	 * 
	 */
	ProductDTO save(ProductDTO product) throws Exception;
    ProductDTO find(String _id) throws Exception;
    List<ProductDTO> list(String filter) throws Exception;
    boolean delete(String _id) throws Exception; //TODO: needs to be a response object
    int count(String filter) throws Exception;
}
