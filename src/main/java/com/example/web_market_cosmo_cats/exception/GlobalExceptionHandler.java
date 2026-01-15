package com.example.web_market_cosmo_cats.exception;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.web_market_cosmo_cats.dto.ErrorResponse;
import com.example.web_market_cosmo_cats.service.exceptions.ProductNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex,
			HttpServletRequest request) {

		Map<String, String> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
				.collect(Collectors.toMap(FieldError::getField,
						fieldError -> fieldError.getDefaultMessage() != null
								? fieldError.getDefaultMessage()
								: "Validation failed",
						(existing, replacement) -> existing + "; " + replacement));

		ErrorResponse errorResponse = ErrorResponse.of("https://example.com/problems/validation-error",
				"Validation Failed", HttpStatus.BAD_REQUEST.value(),
				"The request contains validation errors. Please check the 'errors' field for details.",
				request.getRequestURI(), fieldErrors);

		return ResponseEntity.badRequest().body(errorResponse);
	}

	@ExceptionHandler(ProductNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleNotFound(ProductNotFoundException ex, HttpServletRequest request) {

		ErrorResponse errorResponse = ErrorResponse.of("https://example.com/problems/product-not-found",
				"Product Not Found", HttpStatus.NOT_FOUND.value(), ex.getMessage(), request.getRequestURI(), null);

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpServletRequest request) {

		Throwable cause = ex.getCause();
		String message = (cause != null && cause.getMessage() != null) ? cause.getMessage() : ex.getMessage();

		ParseResult parseResult = ErrorMessageParser.parseDeserializationError(message);

		ErrorResponse errorResponse = ErrorResponse.of("https://example.com/problems/invalid-request-body",
				"Invalid Request Body", HttpStatus.BAD_REQUEST.value(), parseResult.detail(), request.getRequestURI(),
				parseResult.fieldErrors());

		return ResponseEntity.badRequest().body(errorResponse);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex,
			HttpServletRequest request) {

		ErrorResponse errorResponse = ErrorResponse.of("https://example.com/problems/invalid-argument",
				"Invalid Argument", HttpStatus.BAD_REQUEST.value(), ex.getMessage(), request.getRequestURI(), null);

		return ResponseEntity.badRequest().body(errorResponse);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, HttpServletRequest request) {

		ErrorResponse errorResponse = ErrorResponse.of("https://example.com/problems/internal-server-error",
				"Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR.value(),
				"An unexpected error occurred: " + ex.getMessage(), request.getRequestURI(), null);

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
	}
}
