package com.example.web_market_cosmo_cats.dto.Cart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemResponse {
	private String productId;
	private String productName;
	private BigDecimal productPrice;
	private Integer quantity;
	private BigDecimal totalPrice;
}
