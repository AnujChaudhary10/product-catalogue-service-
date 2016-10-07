package com.service.product.catalogue.dto;

public class CreateProductDTO extends ProductDTO{

	private static final long serialVersionUID = -7308066085039264999L;
	private Double price;
	
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}

}
