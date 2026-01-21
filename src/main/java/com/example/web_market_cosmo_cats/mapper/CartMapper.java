package com.example.web_market_cosmo_cats.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.web_market_cosmo_cats.domain.Cart;
import com.example.web_market_cosmo_cats.domain.CartItem;
import com.example.web_market_cosmo_cats.dto.Cart.CartItemResponse;
import com.example.web_market_cosmo_cats.dto.Cart.CartResponse;

@Mapper(componentModel = "spring")
public interface CartMapper {

	@Mapping(target = "items", expression = "java(mapCartItems(cart.getItems()))")
	@Mapping(target = "itemCount", expression = "java(cart.getItems().size())")
	CartResponse toResponse(Cart cart);

	List<CartResponse> toResponseList(List<Cart> carts);

	default List<CartItemResponse> mapCartItems(List<CartItem> cartItems) {
		if (cartItems == null) {
			return List.of();
		}
		return cartItems.stream().map(this::mapCartItem).collect(Collectors.toList());
	}

	default CartItemResponse mapCartItem(CartItem cartItem) {
		return new CartItemResponse(cartItem.getProduct().getId(), cartItem.getProduct().getName(),
				cartItem.getProduct().getPrice(), cartItem.getQuantity(),
				cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
	}
}
