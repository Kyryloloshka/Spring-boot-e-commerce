package com.example.web_market_cosmo_cats.featureToggles;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = {"feature.shoppingCart.enabled=true"})
class FeatureToggleServiceTest {

	@Autowired
	private FeatureToggleService featureToggleService;

	@Test
	void shouldReturnTrueForEnabledShoppingCartFeature() {
		assertThat(featureToggleService.isFeatureEnabled("shoppingCart")).isTrue();
	}

	@Test
	void shouldReturnFalseForUnknownFeature() {
		assertThat(featureToggleService.isFeatureEnabled("unknownFeature")).isFalse();
	}

	@Test
	void shouldReturnTrueForShoppingCartEnabledViaValueAnnotation() {
		assertThat(featureToggleService.isShoppingCartEnabled()).isTrue();
	}

}
