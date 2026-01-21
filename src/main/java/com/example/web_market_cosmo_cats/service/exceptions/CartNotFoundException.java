package com.example.web_market_cosmo_cats.service.exceptions;

public class CartNotFoundException extends RuntimeException {

	public CartNotFoundException(String message) {
		super(message);
	}

	public CartNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
