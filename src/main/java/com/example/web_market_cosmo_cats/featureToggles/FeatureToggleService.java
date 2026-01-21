package com.example.web_market_cosmo_cats.featureToggles;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeatureToggleService {

	private final Environment environment;
	private final Map<String, Boolean> featureCache = new ConcurrentHashMap<>();

	@Value("${feature.shoppingCart.enabled:true}")
	private boolean shoppingCartEnabled;

	public boolean isFeatureEnabled(String featureName) {
		return featureCache.computeIfAbsent(featureName, this::loadFeatureFromEnvironment);
	}

	private boolean loadFeatureFromEnvironment(String featureName) {
		String propertyKey = "feature." + featureName + ".enabled";
		String propertyValue = environment.getProperty(propertyKey);
		return Boolean.parseBoolean(propertyValue != null ? propertyValue : "false");
	}

	public boolean isShoppingCartEnabled() {
		return shoppingCartEnabled;
	}
}
