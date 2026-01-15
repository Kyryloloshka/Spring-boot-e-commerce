package com.example.web_market_cosmo_cats.controller;

import com.example.web_market_cosmo_cats.dto.ProductRequest;
import com.example.web_market_cosmo_cats.dto.ProductResponse;
import com.example.web_market_cosmo_cats.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
	private final ProductService service;

	@GetMapping
	public ResponseEntity<List<ProductResponse>> getAll() {
		List<ProductResponse> products = service.getAll();
		return ResponseEntity.ok(products);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProductResponse> getById(@PathVariable String id) {
		ProductResponse product = service.getById(id);
		return ResponseEntity.ok(product);
	}

	@PostMapping
	public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest request) {
		ProductResponse product = service.create(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(product);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ProductResponse> update(@PathVariable String id, @Valid @RequestBody ProductRequest request) {
		ProductResponse product = service.update(id, request);
		return ResponseEntity.ok(product);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable String id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
