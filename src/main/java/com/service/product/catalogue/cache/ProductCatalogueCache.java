package com.service.product.catalogue.cache;

import java.util.Set;

public interface ProductCatalogueCache<K, V> {
	public V getValue(K key);

	public void put(K key, V value);

	public void remove(K key);

	public Set<K> getExistingKeys();

}
