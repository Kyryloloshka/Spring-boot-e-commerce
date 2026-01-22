package com.example.web_market_cosmo_cats.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
	private String id;
	private String name;
	private String description;
}
