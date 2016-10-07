package com.service.product.catalogue.service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.service.product.catalogue.cache.ProductCatalogueCache;
import com.service.product.catalogue.common.ServiceConstant;
import com.service.product.catalogue.dao.ProductDao;
import com.service.product.catalogue.dto.CreateProductDTO;
import com.service.product.catalogue.dto.ProductDTO;
import com.service.product.catalogue.exception.ProductDaoException;

@Component
public class ProductCatalogueServiceImpl implements ProductCatalogueService {

	@Autowired
	private Environment env; 
	
	@Autowired
	private ProductDao dao;

	@Autowired
	private ProductCatalogueCache<Integer, ProductDTO> catalogue;

	@Autowired
	private RestTemplate restTemplet;

	@Override
	public Set<ProductDTO> findProducts(Map<String, String> criteria) {
		Set<ProductDTO> products = new HashSet<>();
		Set<Integer> keys = catalogue.getExistingKeys();
		for (Integer key : keys) {
			ProductDTO product = catalogue.getValue(key);
			filter(product, criteria, products);
		}
		return products;
	}

	private void filter(ProductDTO product, Map<String, String> criteria,
			Set<ProductDTO> products) {
		if (!CollectionUtils.isEmpty(criteria)) {
			for (String property : criteria.keySet()) {
				switch (property) {
				case "productName":
					if (product.getProductName().equalsIgnoreCase(
							criteria.get(property))) {
						products.add(product);
					}
					break;
				case "productType":
					if (product.getProductType().equalsIgnoreCase(
							criteria.get(property))) {
						products.add(product);
					}
					break;
				case "productId":
					if (product.getProductId() == Integer.valueOf(criteria
							.get(property))) {
						products.add(product);
					}
					break;
				default:
					products.add(product);
					break;
				}
			}
		} else {
			products.add(product);
		}

	}

	@Override
	public String addProduct(List<CreateProductDTO> products)
			throws ProductDaoException {
		String statusMessage = dao.addProduct(products);
		if (ServiceConstant.CREATED.equalsIgnoreCase(statusMessage)) {
			
			for (ProductDTO product : products) {
				catalogue.put(product.getProductId(), product);
			}
			createProductInPricingService(products);
		}
		return statusMessage;
	}

	@Override
	public String deleteProduct(int productId) {
		String statusMessage = dao.deleteProduct(productId);
		if (ServiceConstant.SUCCESS.equalsIgnoreCase(statusMessage))
			catalogue.remove(productId);
		return statusMessage;
	}

	private void createProductInPricingService(List<CreateProductDTO> priceDTO) throws ProductDaoException {
		HttpEntity<String> httpEntity = createEntityForService(convertObjectToJSON(priceDTO), MediaType.APPLICATION_JSON);
		try{
		 restTemplet.postForObject(env.getProperty("pricing.service.url"), httpEntity,
				String.class);
		}catch(ResourceAccessException ex){
			throw new ProductDaoException(ex.getMessage());
		}
	}

	private HttpEntity<String> createEntityForService(String body,
			MediaType mediaType) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(mediaType);
		HttpEntity<String> httpEntity = new HttpEntity<String>(body, headers);
		return httpEntity;
	}

	private String convertObjectToJSON(Object objects) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(objects);
		} catch (Exception e) {
			return null;
		}
	}
}
