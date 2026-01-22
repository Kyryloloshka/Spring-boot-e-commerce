package com.example.web_market_cosmo_cats.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CosmicWordValidator.class)
public @interface CosmicWordCheck {
	String message() default "Name must contain cosmic words";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
