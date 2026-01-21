package com.example.web_market_cosmo_cats.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.web_market_cosmo_cats.dto.Cart.AddToCartRequest;
import com.example.web_market_cosmo_cats.dto.Cart.CartResponse;
import com.example.web_market_cosmo_cats.service.CartService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {
	private final CartService service;

	@GetMapping
	public ResponseEntity<List<CartResponse>> getAllCarts() {
		List<CartResponse> carts = service.getAllCarts();
		return ResponseEntity.ok(carts);
	}

	@GetMapping("/{id}")
	public ResponseEntity<CartResponse> getCart(@PathVariable String id) {
		CartResponse cart = service.getCart(id);
		return ResponseEntity.ok(cart);
	}

	@PostMapping
	public ResponseEntity<CartResponse> createCart() {
		CartResponse cart = service.createCart();
		return ResponseEntity.status(HttpStatus.CREATED).body(cart);
	}

	@PostMapping("/{cartId}/items")
	public ResponseEntity<CartResponse> addToCart(@PathVariable String cartId,
			@Valid @RequestBody AddToCartRequest request) {
		CartResponse cart = service.addToCart(cartId, request.getProductId());
		return ResponseEntity.ok(cart);
	}

	@DeleteMapping("/{cartId}/items/{productId}")
	public ResponseEntity<CartResponse> removeFromCart(@PathVariable String cartId, @PathVariable String productId) {
		CartResponse cart = service.removeFromCart(cartId, productId);
		return ResponseEntity.ok(cart);
	}

	@PutMapping("/{id}/clear")
	public ResponseEntity<Void> clearCart(@PathVariable String id) {
		service.clearCart(id);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteCart(@PathVariable String id) {
		service.deleteCart(id);
		return ResponseEntity.noContent().build();
	}
}
