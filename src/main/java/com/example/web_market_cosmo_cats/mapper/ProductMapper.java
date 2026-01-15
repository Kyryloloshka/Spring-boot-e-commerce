package com.example.web_market_cosmo_cats.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.example.web_market_cosmo_cats.domain.Product;
import com.example.web_market_cosmo_cats.dto.ProductRequest;
import com.example.web_market_cosmo_cats.dto.ProductResponse;

@Mapper(componentModel = "spring")
public interface ProductMapper {
	Product toEntity(ProductRequest request);

	ProductResponse toResponse(Product product);

	List<ProductResponse> toResponseList(List<Product> products);
}
