package com.example.web_market_cosmo_cats.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.web_market_cosmo_cats.domain.Product;
import com.example.web_market_cosmo_cats.dto.Product.ProductRequest;
import com.example.web_market_cosmo_cats.dto.Product.ProductResponse;
import com.example.web_market_cosmo_cats.mapper.ProductMapper;
import com.example.web_market_cosmo_cats.repository.ProductRepository;
import com.example.web_market_cosmo_cats.service.exceptions.ProductNotFoundException;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

	@Mock
	private ProductRepository repository;

	@Mock
	private ProductMapper mapper;

	@InjectMocks
	private ProductService service;

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

	@Test
	void create_ShouldReturnProductResponse_WhenValidRequest() {
		when(mapper.toEntity(productRequest)).thenReturn(product);
		when(repository.save(product)).thenReturn(product);
		when(mapper.toResponse(product)).thenReturn(productResponse);

		ProductResponse result = service.create(productRequest);

		assertThat(result).isEqualTo(productResponse);
		verify(mapper).toEntity(productRequest);
		verify(repository).save(product);
		verify(mapper).toResponse(product);
	}

	@Test
	void getAll_ShouldReturnListOfProductResponses_WhenProductsExist() {
		List<Product> products = Arrays.asList(product);
		List<ProductResponse> productResponses = Arrays.asList(productResponse);

		when(repository.findAll()).thenReturn(products);
		when(mapper.toResponseList(products)).thenReturn(productResponses);

		List<ProductResponse> result = service.getAll();

		assertThat(result).isEqualTo(productResponses);
		verify(repository).findAll();
		verify(mapper).toResponseList(products);
	}

	@Test
	void getById_ShouldReturnProductResponse_WhenProductExists() {
		when(repository.findById("123")).thenReturn(Optional.of(product));
		when(mapper.toResponse(product)).thenReturn(productResponse);

		ProductResponse result = service.getById("123");

		assertThat(result).isEqualTo(productResponse);
		verify(repository).findById("123");
		verify(mapper).toResponse(product);
	}

	@Test
	void getById_ShouldThrowException_WhenProductNotFound() {
		when(repository.findById("999")).thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.getById("999"))
			.isInstanceOf(ProductNotFoundException.class)
			.hasMessage("Product not found with id: 999");

		verify(repository).findById("999");
	}

	@Test
	void update_ShouldReturnUpdatedProductResponse_WhenProductExists() {
		ProductRequest updateRequest = new ProductRequest();
		updateRequest.setName("Updated Product");
		updateRequest.setDescription("Updated description");
		updateRequest.setPrice(199.99);
		updateRequest.setCategory("Updated Category");

		Product updatedProduct = new Product();
		updatedProduct.setId("123");
		updatedProduct.setName("Updated Product");
		updatedProduct.setDescription("Updated description");
		updatedProduct.setPrice(199.99);
		updatedProduct.setCategory("Updated Category");

		ProductResponse updatedResponse = new ProductResponse();
		updatedResponse.setId("123");
		updatedResponse.setName("Updated Product");
		updatedResponse.setDescription("Updated description");
		updatedResponse.setPrice(199.99);
		updatedResponse.setCategory("Updated Category");

		when(repository.findById("123")).thenReturn(Optional.of(product));
		when(repository.save(any(Product.class))).thenReturn(updatedProduct);
		when(mapper.toResponse(updatedProduct)).thenReturn(updatedResponse);

		ProductResponse result = service.update("123", updateRequest);

		assertThat(result).isEqualTo(updatedResponse);
		assertThat(product.getName()).isEqualTo("Updated Product");
		assertThat(product.getDescription()).isEqualTo("Updated description");
		assertThat(product.getPrice()).isEqualTo(199.99);
		assertThat(product.getCategory()).isEqualTo("Updated Category");

		verify(repository).findById("123");
		verify(repository).save(product);
		verify(mapper).toResponse(updatedProduct);
	}

	@Test
	void update_ShouldThrowException_WhenProductNotFound() {
		ProductRequest updateRequest = new ProductRequest();
		updateRequest.setName("Updated Product");

		when(repository.findById("999")).thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.update("999", updateRequest)).isInstanceOf(ProductNotFoundException.class)
				.hasMessage("Product not found with id: 999");

		verify(repository).findById("999");
	}

	@Test
	void delete_ShouldCallRepositoryDelete_WhenProductExists() {
		service.delete("123");

		verify(repository).deleteById("123");
	}
}
