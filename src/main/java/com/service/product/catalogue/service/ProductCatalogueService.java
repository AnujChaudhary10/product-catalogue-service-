package com.service.product.catalogue.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.service.product.catalogue.dto.CreateProductDTO;
import com.service.product.catalogue.dto.ProductDTO;
import com.service.product.catalogue.exception.ProductDaoException;

public interface ProductCatalogueService {

	Set<ProductDTO> findProducts(Map<String,String> criteria);

	String addProduct(List<CreateProductDTO> product) throws ProductDaoException;

	String deleteProduct(int productId);
}
