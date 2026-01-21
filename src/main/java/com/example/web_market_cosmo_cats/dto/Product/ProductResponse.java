package com.example.web_market_cosmo_cats.dto.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
	private String id;
	private String name;
	private String description;
	private BigDecimal price;
	private String category;
}
