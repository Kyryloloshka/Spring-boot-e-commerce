package com.example.web_market_cosmo_cats.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.example.web_market_cosmo_cats.featureToggles.exception.FeatureNotAvailableException;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = "feature.shoppingCart.enabled=false")
class CartServiceDisabledTest {

	@Autowired
	private CartService cartService;

	@Test
	void shouldThrowExceptionWhenFeatureIsDisabled() {
		assertThatThrownBy(() -> cartService.getAllCarts()).isInstanceOf(FeatureNotAvailableException.class)
				.hasMessage("Feature 'shoppingCart' is not available");
	}

	@Test
	void shouldThrowExceptionWhenGetCartFeatureIsDisabled() {
		assertThatThrownBy(() -> cartService.getCart("cart-1")).isInstanceOf(FeatureNotAvailableException.class)
				.hasMessage("Feature 'shoppingCart' is not available");
	}

	@Test
	void shouldThrowExceptionWhenAddToCartFeatureIsDisabled() {
		assertThatThrownBy(() -> cartService.addToCart("cart-1", "prod-1"))
				.isInstanceOf(FeatureNotAvailableException.class).hasMessage("Feature 'shoppingCart' is not available");
	}
}
