package com.service.product.catalogue.cache;

import java.util.HashSet;
import java.util.Set;

import net.sf.ehcache.Ehcache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.stereotype.Component;

import com.service.product.catalogue.dto.ProductDTO;
@Component
public class ProductCatalogue implements ProductCatalogueCache<Integer, ProductDTO> {

	@Autowired
	private Cache productCache;
	
	@Override
	public ProductDTO getValue(Integer key) {
		ValueWrapper val = productCache.get(key);
		return val != null && val.get() != null ? (ProductDTO)val.get() : null;
	}

	@Override
	public void put(Integer key, ProductDTO value) {
		productCache.put(key, value);
	}

	@Override
	public void remove(Integer key) {
		productCache.evict(key);
	}

	@Override
	public Set<Integer> getExistingKeys() {
		 return new HashSet<Integer>(((Ehcache) productCache.getNativeCache()).getKeys());
	}

	public void putProductInProductCache(Set<ProductDTO> products) {
		if (products != null) {
			for (ProductDTO product : products) {
				productCache.put(product.getProductId(), product);
			}
		}
	}

}
