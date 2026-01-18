package com.example.web_market_cosmo_cats.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.web_market_cosmo_cats.domain.Product;

class ProductRepositoryTest {

	private ProductRepository repository;

	@BeforeEach
	void setUp() {
		repository = new ProductRepository();
	}

	@Test
	void save_ShouldGenerateId_WhenIdIsNull() {
		Product product = new Product();
		product.setName("Test Product");
		product.setDescription("Test Description");
		product.setPrice(100.0);
		product.setCategory("Test Category");

		Product saved = repository.save(product);

		assertThat(saved.getId()).isNotNull();
		assertThat(saved.getName()).isEqualTo("Test Product");
		assertThat(saved.getDescription()).isEqualTo("Test Description");
		assertThat(saved.getPrice()).isEqualTo(100.0);
		assertThat(saved.getCategory()).isEqualTo("Test Category");
	}

	@Test
	void findById_ShouldReturnProduct_WhenExists() {
		Product product = new Product();
		product.setName("Test Product");
		Product saved = repository.save(product);

		Optional<Product> result = repository.findById(saved.getId());

		assertThat(result).isPresent();
		assertThat(result.get().getId()).isEqualTo(saved.getId());
		assertThat(result.get().getName()).isEqualTo("Test Product");
	}


	@Test
	void findAll_ShouldReturnAllProducts() {
		Product product1 = new Product();
		product1.setName("Product 1");
		product1.setPrice(10.0);

		Product product2 = new Product();
		product2.setName("Product 2");
		product2.setPrice(20.0);

		repository.save(product1);
		repository.save(product2);

		List<Product> result = repository.findAll();

		assertThat(result).hasSize(2);
		assertThat(result).extracting(Product::getName).containsExactlyInAnyOrder("Product 1", "Product 2");
	}

	@Test
	void deleteById_ShouldRemoveProduct_WhenExists() {
		Product product = new Product();
		product.setName("Product to Delete");
		Product saved = repository.save(product);

		assertThat(repository.findById(saved.getId())).isPresent();

		repository.deleteById(saved.getId());

		assertThat(repository.findById(saved.getId())).isEmpty();
	}
}
