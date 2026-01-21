package com.example.web_market_cosmo_cats.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.web_market_cosmo_cats.domain.Cart;
import com.example.web_market_cosmo_cats.domain.CartItem;
import com.example.web_market_cosmo_cats.domain.Product;
import com.example.web_market_cosmo_cats.dto.Cart.CartResponse;
import com.example.web_market_cosmo_cats.featureToggles.FeatureToggle;
import com.example.web_market_cosmo_cats.mapper.CartMapper;
import com.example.web_market_cosmo_cats.repository.CartRepository;
import com.example.web_market_cosmo_cats.repository.ProductRepository;
import com.example.web_market_cosmo_cats.service.exceptions.CartNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {
	private final CartRepository cartRepository;
	private final ProductRepository productRepository;
	private final CartMapper mapper;

	@FeatureToggle(feature = "shoppingCart")
	@Transactional(readOnly = true)
	public CartResponse getCart(String cartId) {
		Cart cart = cartRepository.findByIdWithItems(cartId)
				.orElseThrow(() -> new CartNotFoundException("Cart not found with id: " + cartId));

		return mapper.toResponse(cart);
	}

	@FeatureToggle(feature = "shoppingCart")
	@Transactional(readOnly = true)
	public List<CartResponse> getAllCarts() {
		List<Cart> carts = cartRepository.findAll();
		return mapper.toResponseList(carts);
	}

	@FeatureToggle(feature = "shoppingCart")
	@Transactional
	public CartResponse createCart() {
		Cart cart = new Cart();
		cart.setTotalPrice(BigDecimal.ZERO);

		Cart saved = cartRepository.save(cart);
		return mapper.toResponse(saved);
	}

	@FeatureToggle(feature = "shoppingCart")
	@Transactional
	public CartResponse addToCart(String cartId, String productId) {
		Cart cart = cartRepository.findByIdWithItems(cartId)
				.orElseThrow(() -> new CartNotFoundException("Cart not found with id: " + cartId));

		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

		// Check if product already exists in cart
		CartItem existingItem = cart.getItems().stream().filter(item -> item.getProduct().getId().equals(productId))
				.findFirst().orElse(null);

		if (existingItem != null) {
			existingItem.setQuantity(existingItem.getQuantity() + 1);
		} else {
			cart.addProduct(product);
		}

		Cart updated = cartRepository.save(cart);
		return mapper.toResponse(updated);
	}

	@FeatureToggle(feature = "shoppingCart")
	@Transactional
	public CartResponse removeFromCart(String cartId, String productId) {
		Cart cart = cartRepository.findByIdWithItems(cartId)
				.orElseThrow(() -> new CartNotFoundException("Cart not found with id: " + cartId));

		cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
		cart.recalculateTotalPrice();

		Cart updated = cartRepository.save(cart);
		return mapper.toResponse(updated);
	}

	@FeatureToggle(feature = "shoppingCart")
	@Transactional
	public void clearCart(String cartId) {
		Cart cart = cartRepository.findById(cartId)
				.orElseThrow(() -> new CartNotFoundException("Cart not found with id: " + cartId));

		cart.getItems().clear();
		cart.setTotalPrice(BigDecimal.ZERO);

		cartRepository.save(cart);
	}

	@FeatureToggle(feature = "shoppingCart")
	@Transactional
	public void deleteCart(String cartId) {
		cartRepository.deleteById(cartId);
	}
}
