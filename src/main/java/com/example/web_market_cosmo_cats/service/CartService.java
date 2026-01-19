package com.example.web_market_cosmo_cats.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.web_market_cosmo_cats.domain.Cart;
import com.example.web_market_cosmo_cats.dto.Cart.CartResponse;
import com.example.web_market_cosmo_cats.dto.Product.ProductResponse;
import com.example.web_market_cosmo_cats.featureToggles.FeatureToggle;
import com.example.web_market_cosmo_cats.mapper.CartMapper;
import com.example.web_market_cosmo_cats.repository.CartRepository;
import com.example.web_market_cosmo_cats.service.exceptions.CartNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {
	private final CartRepository repository;
	private final CartMapper mapper;
	private final ProductService productService;

	@FeatureToggle(feature = "shoppingCart")
	public CartResponse getCart(String cartId) {
		Cart cart = repository.findById(cartId)
				.orElseThrow(() -> new CartNotFoundException("Cart not found with id: " + cartId));

		return mapper.toResponse(cart);
	}

	@FeatureToggle(feature = "shoppingCart")
	public List<CartResponse> getAllCarts() {
		List<Cart> carts = repository.findAll();
		return mapper.toResponseList(carts);
	}

	@FeatureToggle(feature = "shoppingCart")
	public CartResponse createCart() {
		Cart cart = new Cart();
		cart.setProductIds(new ArrayList<>());
		cart.setTotalPrice(0.0);

		Cart saved = repository.save(cart);
		return mapper.toResponse(saved);
	}

	@FeatureToggle(feature = "shoppingCart")
	public CartResponse addToCart(String cartId, String productId) {
		Cart cart = repository.findById(cartId)
				.orElseThrow(() -> new CartNotFoundException("Cart not found with id: " + cartId));

		ProductResponse product = productService.getById(productId);
		cart.getProductIds().add(productId);
		cart.setTotalPrice(cart.getTotalPrice() + product.getPrice());

		Cart updated = repository.save(cart);
		return mapper.toResponse(updated);
	}

	@FeatureToggle(feature = "shoppingCart")
	public CartResponse removeFromCart(String cartId, String productId) {
		Cart cart = repository.findById(cartId)
				.orElseThrow(() -> new CartNotFoundException("Cart not found with id: " + cartId));

		Double productPrice = 0.0;
		try {
			ProductResponse product = productService.getById(productId);
			productPrice = product.getPrice();
		} catch (Exception e) {
		}

		cart.getProductIds().remove(productId);
		cart.setTotalPrice(Math.max(0, cart.getTotalPrice() - productPrice));

		Cart updated = repository.save(cart);
		return mapper.toResponse(updated);
	}

	@FeatureToggle(feature = "shoppingCart")
	public void clearCart(String cartId) {
		Cart cart = repository.findById(cartId)
				.orElseThrow(() -> new CartNotFoundException("Cart not found with id: " + cartId));

		cart.getProductIds().clear();
		cart.setTotalPrice(0.0);

		repository.save(cart);
	}

	@FeatureToggle(feature = "shoppingCart")
	public void deleteCart(String cartId) {
		repository.deleteById(cartId);
	}
}
