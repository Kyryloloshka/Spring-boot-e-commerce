package com.example.web_market_cosmo_cats.exception;

import java.net.URI;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.web_market_cosmo_cats.service.exceptions.ProductNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ProblemDetail> handleValidation(MethodArgumentNotValidException ex,
			HttpServletRequest request) {

		Map<String, String> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
				.collect(Collectors.toMap(FieldError::getField,
						fieldError -> fieldError.getDefaultMessage() != null
								? fieldError.getDefaultMessage()
								: "Validation failed",
						(existing, replacement) -> existing + "; " + replacement));

		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
				"The request contains validation errors. Please check the errors field for details.");
		problemDetail.setType(URI.create("https://example.com/problems/validation-error"));
		problemDetail.setTitle("Validation Failed");
		problemDetail.setInstance(URI.create(request.getRequestURI()));
		problemDetail.setProperty("errors", fieldErrors);

		return ResponseEntity.badRequest().body(problemDetail);
	}

	@ExceptionHandler(ProductNotFoundException.class)
	public ResponseEntity<ProblemDetail> handleNotFound(ProductNotFoundException ex, HttpServletRequest request) {

		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
		problemDetail.setType(URI.create("https://example.com/problems/product-not-found"));
		problemDetail.setTitle("Product Not Found");
		problemDetail.setInstance(URI.create(request.getRequestURI()));

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ProblemDetail> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpServletRequest request) {

		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
				"Invalid JSON format. Please check your request body syntax.");
		problemDetail.setType(URI.create("https://example.com/problems/invalid-request-body"));
		problemDetail.setTitle("Invalid Request Body");
		problemDetail.setInstance(URI.create(request.getRequestURI()));

		return ResponseEntity.badRequest().body(problemDetail);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ProblemDetail> handleIllegalArgument(IllegalArgumentException ex,
			HttpServletRequest request) {

		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
		problemDetail.setType(URI.create("https://example.com/problems/invalid-argument"));
		problemDetail.setTitle("Invalid Argument");
		problemDetail.setInstance(URI.create(request.getRequestURI()));

		return ResponseEntity.badRequest().body(problemDetail);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ProblemDetail> handleGenericException(Exception ex, HttpServletRequest request) {

		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR,
				"An unexpected error occurred: " + ex.getMessage());
		problemDetail.setType(URI.create("https://example.com/problems/internal-server-error"));
		problemDetail.setTitle("Internal Server Error");
		problemDetail.setInstance(URI.create(request.getRequestURI()));

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problemDetail);
	}
}
