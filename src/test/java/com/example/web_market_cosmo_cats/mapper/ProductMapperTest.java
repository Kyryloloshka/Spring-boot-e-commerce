package com.example.web_market_cosmo_cats.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.example.web_market_cosmo_cats.domain.Product;
import com.example.web_market_cosmo_cats.dto.ProductRequest;
import com.example.web_market_cosmo_cats.dto.ProductResponse;

class ProductMapperTest {

	private ProductMapper mapper;

	@BeforeEach
	void setUp() {
		mapper = Mappers.getMapper(ProductMapper.class);
	}

	@Test
	void toEntity_ShouldMapAllFieldsCorrectly() {
		ProductRequest request = new ProductRequest();
		request.setName("Star Product");
		request.setDescription("Amazing cosmic product");
		request.setPrice(99.99);
		request.setCategory("Electronics");

		Product result = mapper.toEntity(request);

		assertThat(result).isNotNull();
		assertThat(result.getId()).isNull(); // ID should not be set from request
		assertThat(result.getName()).isEqualTo("Star Product");
		assertThat(result.getDescription()).isEqualTo("Amazing cosmic product");
		assertThat(result.getPrice()).isEqualTo(99.99);
		assertThat(result.getCategory()).isEqualTo("Electronics");
	}

	@Test
	void toResponse_ShouldMapAllFieldsCorrectly() {
		Product product = new Product();
		product.setId("123");
		product.setName("Star Product");
		product.setDescription("Amazing cosmic product");
		product.setPrice(99.99);
		product.setCategory("Electronics");

		ProductResponse result = mapper.toResponse(product);

		assertThat(result).isNotNull();
		assertThat(result.getId()).isEqualTo("123");
		assertThat(result.getName()).isEqualTo("Star Product");
		assertThat(result.getDescription()).isEqualTo("Amazing cosmic product");
		assertThat(result.getPrice()).isEqualTo(99.99);
		assertThat(result.getCategory()).isEqualTo("Electronics");
	}
}
