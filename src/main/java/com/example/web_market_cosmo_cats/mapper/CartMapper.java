package com.example.web_market_cosmo_cats.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.web_market_cosmo_cats.domain.Cart;
import com.example.web_market_cosmo_cats.dto.Cart.CartResponse;

@Mapper(componentModel = "spring")
public interface CartMapper {
	@Mapping(target = "itemCount", expression = "java(cart.getProductIds().size())")
	CartResponse toResponse(Cart cart);

	List<CartResponse> toResponseList(List<Cart> carts);
}
