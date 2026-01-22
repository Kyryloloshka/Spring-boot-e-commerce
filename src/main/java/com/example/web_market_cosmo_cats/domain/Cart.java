package com.example.web_market_cosmo_cats.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
	private String id;
	private List<String> productIds;
	private Double totalPrice;
}
