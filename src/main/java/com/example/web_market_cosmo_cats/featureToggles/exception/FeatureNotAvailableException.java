package com.example.web_market_cosmo_cats.featureToggles.exception;

public class FeatureNotAvailableException extends RuntimeException {

	public FeatureNotAvailableException(String message) {
		super(message);
	}

	public FeatureNotAvailableException(String message, Throwable cause) {
		super(message, cause);
	}
}
