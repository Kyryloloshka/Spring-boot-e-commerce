package com.example.web_market_cosmo_cats.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.example.web_market_cosmo_cats.domain.Cart;

@Repository
public class CartRepository {
	private final Map<String, Cart> storage = new HashMap<>();

	public Cart save(Cart cart) {
		if (cart.getId() == null) {
			cart.setId(UUID.randomUUID().toString());
		}

		storage.put(cart.getId(), cart);

		return cart;
	}

	public Optional<Cart> findById(String id) {
		return Optional.ofNullable(storage.get(id));
	}

	public List<Cart> findAll() {
		return new ArrayList<>(storage.values());
	}

	public void deleteById(String id) {
		storage.remove(id);
	}

	public boolean existsById(String id) {
		return storage.containsKey(id);
	}
}
