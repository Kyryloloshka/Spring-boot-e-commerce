package com.example.web_market_cosmo_cats.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.web_market_cosmo_cats.domain.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

	List<Product> findByCategory(String category);

	List<Product> findByNameContainingIgnoreCase(String name);

	Optional<Product> findByName(String name);
}
