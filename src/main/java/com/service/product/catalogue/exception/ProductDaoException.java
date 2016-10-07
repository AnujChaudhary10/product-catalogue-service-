package com.service.product.catalogue.exception;

public class ProductDaoException extends Exception{
	
	private static final long serialVersionUID = 7537616283946030765L;

	public ProductDaoException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProductDaoException(String message) {
		super(message);

	}
}
