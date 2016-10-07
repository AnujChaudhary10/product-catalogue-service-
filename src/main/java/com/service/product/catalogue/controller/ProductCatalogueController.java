package com.service.product.catalogue.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.service.product.catalogue.common.ServiceConstant;
import com.service.product.catalogue.dto.CreateProductDTO;
import com.service.product.catalogue.dto.ProductDTO;
import com.service.product.catalogue.exception.ProductDaoException;
import com.service.product.catalogue.service.ProductCatalogueService;



//import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
public class ProductCatalogueController {
	
	@Autowired
	private ProductCatalogueService catalogueService;

	@RequestMapping(value = "/products/search", method = RequestMethod.GET, produces = "application/json")
	public Set<ProductDTO> getProduct(@RequestParam Map<String,String> requestCriteria){
		 return catalogueService.findProducts(requestCriteria);
	}
	
	@RequestMapping(value = "/products", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<String> addProduct(@RequestBody List<CreateProductDTO> product){
		String message = ServiceConstant.FAIL;
		try{
			message = catalogueService.addProduct(product);
		}catch(ProductDaoException ex){
			message = ex.getMessage();
		}
		if(message.equalsIgnoreCase(ServiceConstant.CREATED)){
			return new ResponseEntity<String>(message,HttpStatus.CREATED);
		}
		return new ResponseEntity<String>(message,HttpStatus.BAD_REQUEST);
	}
	
	@RequestMapping(value = "/products/{productId}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteProduct(@PathVariable("productId") int productId){
		String message = catalogueService.deleteProduct(productId);
		if(message.equalsIgnoreCase(ServiceConstant.SUCCESS)){
			return new ResponseEntity<String>(message,HttpStatus.OK);
		}
		return new ResponseEntity<String>(message,HttpStatus.BAD_REQUEST);
	}
	
}
