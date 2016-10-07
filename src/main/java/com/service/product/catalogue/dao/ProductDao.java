package com.service.product.catalogue.dao;

import java.util.List;
import java.util.Set;

import com.service.product.catalogue.dto.CreateProductDTO;
import com.service.product.catalogue.dto.ProductDTO;
import com.service.product.catalogue.exception.ProductDaoException;

public interface ProductDao {

	Set<ProductDTO> findProduct();
	
	String addProduct(List<CreateProductDTO> product) throws ProductDaoException;
	
	String deleteProduct(int productId);
}
