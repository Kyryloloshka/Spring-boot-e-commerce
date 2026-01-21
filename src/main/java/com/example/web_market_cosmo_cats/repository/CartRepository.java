package com.example.web_market_cosmo_cats.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.web_market_cosmo_cats.domain.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, String> {

	@Query("SELECT c FROM Cart c LEFT JOIN FETCH c.items WHERE c.id = :id")
	Optional<Cart> findByIdWithItems(@Param("id") String id);
}
