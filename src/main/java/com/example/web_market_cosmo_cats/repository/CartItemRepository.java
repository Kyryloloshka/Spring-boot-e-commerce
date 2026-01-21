package com.example.web_market_cosmo_cats.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.web_market_cosmo_cats.domain.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, String> {

	List<CartItem> findByCartId(String cartId);

	void deleteByCartId(String cartId);
}
