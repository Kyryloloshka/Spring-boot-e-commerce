package com.example.web_market_cosmo_cats.service;

import java.util.List;

import com.example.web_market_cosmo_cats.dto.ProductRequest;
import com.example.web_market_cosmo_cats.dto.ProductResponse;

public interface ProductService {
	ProductResponse create(ProductRequest request);

	List<ProductResponse> getAll();

	ProductResponse getById(String id);

	ProductResponse update(String id, ProductRequest request);

	void delete(String id);
}
