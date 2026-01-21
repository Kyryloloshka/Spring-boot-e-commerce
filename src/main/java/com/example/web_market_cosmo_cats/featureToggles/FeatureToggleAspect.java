package com.example.web_market_cosmo_cats.featureToggles;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.example.web_market_cosmo_cats.featureToggles.exception.FeatureNotAvailableException;

import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class FeatureToggleAspect {

	private final FeatureToggleService featureToggleService;

	@Around("@annotation(featureToggle)")
	public Object checkFeatureToggle(ProceedingJoinPoint joinPoint, FeatureToggle featureToggle) throws Throwable {
		String featureName = featureToggle.feature();

		if (!featureToggleService.isFeatureEnabled(featureName)) {
			throw new FeatureNotAvailableException("Feature '" + featureName + "' is not available");
		}

		return joinPoint.proceed();
	}
}
