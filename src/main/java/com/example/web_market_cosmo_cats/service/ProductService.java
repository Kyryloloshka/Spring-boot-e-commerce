package com.example.web_market_cosmo_cats.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.web_market_cosmo_cats.domain.Product;
import com.example.web_market_cosmo_cats.dto.Product.ProductRequest;
import com.example.web_market_cosmo_cats.dto.Product.ProductResponse;
import com.example.web_market_cosmo_cats.mapper.ProductMapper;
import com.example.web_market_cosmo_cats.repository.ProductRepository;
import com.example.web_market_cosmo_cats.service.exceptions.ProductNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
	private final ProductRepository repository;
	private final ProductMapper mapper;

	public ProductResponse create(ProductRequest request) {
		Product product = mapper.toEntity(request);
		Product saved = repository.save(product);

		return mapper.toResponse(saved);
	}

	public List<ProductResponse> getAll() {
		List<Product> products = repository.findAll();

		return mapper.toResponseList(products);
	}

	public ProductResponse getById(String id) {
		Product product = repository.findById(id)
				.orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));

		return mapper.toResponse(product);
	}

	public ProductResponse update(String id, ProductRequest request) {
		Product existing = repository.findById(id)
				.orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));

		existing.setName(request.getName());
		existing.setDescription(request.getDescription());
		existing.setPrice(request.getPrice());
		existing.setCategory(request.getCategory());

		Product updated = repository.save(existing);

		return mapper.toResponse(updated);
	}

	public void delete(String id) {
		repository.deleteById(id);
	}
}
