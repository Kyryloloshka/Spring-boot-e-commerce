package com.example.web_market_cosmo_cats.repository;

import com.example.web_market_cosmo_cats.domain.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ProductRepository {
	private final Map<String, Product> storage = new HashMap<>();

	public Product save(Product product) {
		if (product.getId() == null) {
			product.setId(UUID.randomUUID().toString());
		}
		storage.put(product.getId(), product);
		return product;
	}

	public Optional<Product> findById(String id) {
		return Optional.ofNullable(storage.get(id));
	}

	public List<Product> findAll() {
		return new ArrayList<>(storage.values());
	}

	public void deleteById(String id) {
		storage.remove(id);
	}
}
