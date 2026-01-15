package com.example.web_market_cosmo_cats.validation;

import java.util.Set;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CosmicWordValidator implements ConstraintValidator<CosmicWordCheck, String> {
	private static final Set<String> COSMIC = Set.of("star", "galaxy", "comet", "cosmic", "space");

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null)
			return false;

		return COSMIC.stream().anyMatch(word -> value.toLowerCase().contains(word));
	}
}
