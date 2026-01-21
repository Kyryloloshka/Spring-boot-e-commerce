package com.example.web_market_cosmo_cats.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

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
		assertThat(cart.getItems()).isEmpty();
		assertThat(cart.getTotalPrice()).isEqualByComparingTo(BigDecimal.ZERO);
		assertThat(cart.getItemCount()).isEqualTo(0);
	}

	@Test
	void shouldReturnAllCartsWhenFeatureIsEnabled() {
		CartResponse cart1 = cartService.createCart();
		CartResponse cart2 = cartService.createCart();

		var carts = cartService.getAllCarts();

		assertThat(carts).isNotNull();
		assertThat(carts).filteredOn(c -> c.getId().equals(cart1.getId()) || c.getId().equals(cart2.getId()))
				.hasSize(2);
		assertThat(carts.stream().map(CartResponse::getId)).contains(cart1.getId(), cart2.getId());
	}

	@Test
	void shouldReturnCartByIdWhenFeatureIsEnabled() {
		CartResponse created = cartService.createCart();

		CartResponse found = cartService.getCart(created.getId());

		assertThat(found).isNotNull();
		assertThat(found.getId()).isEqualTo(created.getId());
		assertThat(found.getItems()).isEmpty();
		assertThat(found.getTotalPrice()).isEqualByComparingTo(BigDecimal.ZERO);
		assertThat(found.getItemCount()).isEqualTo(0);
	}

	@Test
	void shouldAddToCartWhenFeatureIsEnabled() {
		// Create a product first
		ProductRequest productRequest = new ProductRequest();
		productRequest.setName("Star Product");
		productRequest.setDescription("Test Description");
		productRequest.setPrice(BigDecimal.valueOf(29.99));
		productRequest.setCategory("Test Category");

		ProductResponse product = productService.create(productRequest);

		CartResponse cart = cartService.createCart();
		String cartId = cart.getId();

		CartResponse updated = cartService.addToCart(cartId, product.getId());

		assertThat(updated).isNotNull();
		assertThat(updated.getId()).isEqualTo(cartId);
		assertThat(updated.getItems()).isNotEmpty();
		assertThat(updated.getTotalPrice()).isEqualByComparingTo(BigDecimal.valueOf(29.99));
		assertThat(updated.getItemCount()).isEqualTo(1);
	}

	@Test
	void shouldClearCartWhenFeatureIsEnabled() {
		// Create a product first
		ProductRequest productRequest = new ProductRequest();
		productRequest.setName("Star Product");
		productRequest.setDescription("Test Description");
		productRequest.setPrice(BigDecimal.valueOf(29.99));
		productRequest.setCategory("Test Category");

		ProductResponse product = productService.create(productRequest);

		CartResponse cart = cartService.createCart();
		String cartId = cart.getId();

		cartService.addToCart(cartId, product.getId());
		cartService.clearCart(cartId);

		CartResponse cleared = cartService.getCart(cartId);
		assertThat(cleared.getItems()).isEmpty();
		assertThat(cleared.getTotalPrice()).isEqualByComparingTo(BigDecimal.ZERO);
		assertThat(cleared.getItemCount()).isEqualTo(0);
	}
}
