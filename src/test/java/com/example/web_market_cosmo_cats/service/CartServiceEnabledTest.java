package com.example.web_market_cosmo_cats.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import com.example.web_market_cosmo_cats.dto.Cart.CartResponse;
import com.example.web_market_cosmo_cats.dto.Product.ProductRequest;
import com.example.web_market_cosmo_cats.dto.Product.ProductResponse;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestPropertySource(properties = "feature.shoppingCart.enabled=true")
class CartServiceEnabledTest {

	@Autowired
	private CartService cartService;

	@Autowired
	private com.example.web_market_cosmo_cats.service.ProductService productService;

	@Test
	void shouldCreateCartWhenFeatureIsEnabled() {
		CartResponse cart = cartService.createCart();

		assertThat(cart).isNotNull();
		assertThat(cart.getId()).isNotNull();
		assertThat(cart.getProductIds()).isEmpty();
		assertThat(cart.getTotalPrice()).isEqualTo(0.0);
		assertThat(cart.getItemCount()).isEqualTo(0);
	}

	@Test
	void shouldReturnAllCartsWhenFeatureIsEnabled() {
		CartResponse cart1 = cartService.createCart();
		CartResponse cart2 = cartService.createCart();

		var carts = cartService.getAllCarts();

		assertThat(carts).isNotNull();
		assertThat(carts).hasSize(2);
		assertThat(carts.stream().map(CartResponse::getId)).contains(cart1.getId(), cart2.getId());
	}

	@Test
	void shouldReturnCartByIdWhenFeatureIsEnabled() {
		CartResponse created = cartService.createCart();

		CartResponse found = cartService.getCart(created.getId());

		assertThat(found).isNotNull();
		assertThat(found.getId()).isEqualTo(created.getId());
		assertThat(found.getProductIds()).isEmpty();
		assertThat(found.getTotalPrice()).isEqualTo(0.0);
		assertThat(found.getItemCount()).isEqualTo(0);
	}

	@Test
	void shouldAddToCartWhenFeatureIsEnabled() {
		// Create a product first
		ProductRequest productRequest = new ProductRequest();
		productRequest.setName("Star Product");
		productRequest.setDescription("Test Description");
		productRequest.setPrice(29.99);
		productRequest.setCategory("Test Category");

		ProductResponse product = productService.create(productRequest);

		CartResponse cart = cartService.createCart();
		String cartId = cart.getId();

		CartResponse updated = cartService.addToCart(cartId, product.getId());

		assertThat(updated).isNotNull();
		assertThat(updated.getId()).isEqualTo(cartId);
		assertThat(updated.getProductIds()).contains(product.getId());
		assertThat(updated.getTotalPrice()).isEqualTo(29.99);
		assertThat(updated.getItemCount()).isEqualTo(1);
	}

	@Test
	void shouldClearCartWhenFeatureIsEnabled() {
		// Create a product first
		ProductRequest productRequest = new ProductRequest();
		productRequest.setName("Star Product");
		productRequest.setDescription("Test Description");
		productRequest.setPrice(29.99);
		productRequest.setCategory("Test Category");

		ProductResponse product = productService.create(productRequest);

		CartResponse cart = cartService.createCart();
		String cartId = cart.getId();

		cartService.addToCart(cartId, product.getId());
		cartService.clearCart(cartId);

		CartResponse cleared = cartService.getCart(cartId);
		assertThat(cleared.getProductIds()).isEmpty();
		assertThat(cleared.getTotalPrice()).isEqualTo(0.0);
		assertThat(cleared.getItemCount()).isEqualTo(0);
	}
}
