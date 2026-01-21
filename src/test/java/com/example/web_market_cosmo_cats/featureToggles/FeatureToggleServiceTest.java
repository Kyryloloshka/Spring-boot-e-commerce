package com.example.web_market_cosmo_cats.featureToggles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;

@ExtendWith(MockitoExtension.class)
class FeatureToggleServiceTest {

	@Mock
	private Environment environment;

	@InjectMocks
	private FeatureToggleService featureToggleService;

	@Test
	void shouldReturnTrueForEnabledShoppingCartFeature() {
		when(environment.getProperty("feature.shoppingCart.enabled")).thenReturn("true");

		assertThat(featureToggleService.isFeatureEnabled("shoppingCart")).isTrue();
	}

	@Test
	void shouldReturnFalseForUnknownFeature() {
		assertThat(featureToggleService.isFeatureEnabled("unknownFeature")).isFalse();
	}
}
