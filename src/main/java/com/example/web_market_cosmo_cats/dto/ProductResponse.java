package com.example.web_market_cosmo_cats.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
	private String id;
	private String name;
	private String description;
	private Double price;
	private String category;
}
