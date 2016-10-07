package com.service.product.catalogue.cache.loader;

import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.service.product.catalogue.cache.ProductCatalogue;
import com.service.product.catalogue.dao.ProductDao;
import com.service.product.catalogue.dto.ProductDTO;
@Component
public class ProductCacheLoader {
	@Autowired
	private ProductCatalogue productCatalogue;
	@Autowired
	private ProductDao dao;

	@PostConstruct
	public void init() {

		Set<ProductDTO> products = dao.findProduct();
		if (products != null) {
			productCatalogue.putProductInProductCache(products);
		}
	}
}
