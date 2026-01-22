package com.example.web_market_cosmo_cats.service.exceptions;

public class ProductNotFoundException extends RuntimeException {
	public ProductNotFoundException(String message) {
		super(message);
	}
}
