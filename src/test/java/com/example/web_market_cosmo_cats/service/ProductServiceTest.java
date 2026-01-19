package com.example.web_market_cosmo_cats.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.web_market_cosmo_cats.domain.Product;
import com.example.web_market_cosmo_cats.dto.Product.ProductRequest;
import com.example.web_market_cosmo_cats.dto.Product.ProductResponse;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

	private ProductRequest productRequest;
	private Product product;
	private ProductResponse productResponse;

	@BeforeEach
	void setUp() {
		productRequest = new ProductRequest();
		productRequest.setName("Star Product");
		productRequest.setDescription("Amazing cosmic product");
		productRequest.setPrice(99.99);
		productRequest.setCategory("Electronics");

		product = new Product();
		product.setId("123");
		product.setName("Star Product");
		product.setDescription("Amazing cosmic product");
		product.setPrice(99.99);
		product.setCategory("Electronics");

		productResponse = new ProductResponse();
		productResponse.setId("123");
		productResponse.setName("Star Product");
		productResponse.setDescription("Amazing cosmic product");
		productResponse.setPrice(99.99);
		productResponse.setCategory("Electronics");
	}

}
