package com.example.web_market_cosmo_cats.dto.Product;

import com.example.web_market_cosmo_cats.validation.CosmicWordCheck;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
	@NotNull
	@CosmicWordCheck
	private String name;

	@Size(max = 500)
	private String description;

	@Positive
	private BigDecimal price;

	@NotNull
	private String category;
}
