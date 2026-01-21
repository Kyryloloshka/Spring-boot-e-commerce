package com.example.web_market_cosmo_cats.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import com.example.web_market_cosmo_cats.dto.Product.ProductRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ProductControllerIT {

	@Container
	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DockerImageName.parse("postgres:15-alpine"))
			.withDatabaseName("testdb").withUsername("test").withPassword("test");

	@DynamicPropertySource
	static void configureProperties(DynamicPropertyRegistry registry) {
		postgres.start();
		registry.add("spring.datasource.url", postgres::getJdbcUrl);
		registry.add("spring.datasource.username", postgres::getUsername);
		registry.add("spring.datasource.password", postgres::getPassword);
		registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
	}

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	private ObjectMapper objectMapper;

	private ProductRequest validProductRequest;
	private String baseUrl;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		objectMapper = new ObjectMapper();

		validProductRequest = new ProductRequest();
		validProductRequest.setName("Star Product");
		validProductRequest.setDescription("Amazing cosmic product");
		validProductRequest.setPrice(BigDecimal.valueOf(99.99));
		validProductRequest.setCategory("Electronics");

		baseUrl = "/api/products";
	}

	@Test
	void getAll_ShouldReturnEmptyList_WhenNoProductsExist() throws Exception {
		// When & Then
		mockMvc.perform(get(baseUrl)).andExpect(status().isOk()).andExpect(content().json("[]"));
	}

	@Test
	void createAndGetAll_ShouldWorkTogether() throws Exception {
		String productJson = objectMapper.writeValueAsString(validProductRequest);

		mockMvc.perform(post(baseUrl).contentType(MediaType.APPLICATION_JSON).content(productJson))
				.andExpect(status().isCreated());

		mockMvc.perform(get(baseUrl)).andExpect(status().isOk()).andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$.length()").value(1));
	}

	@Test
	void create_ShouldReturnCreatedProduct_WhenValidRequest() throws Exception {
		String productJson = objectMapper.writeValueAsString(validProductRequest);

		mockMvc.perform(post(baseUrl).contentType(MediaType.APPLICATION_JSON).content(productJson))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.name").value("Star Product"))
				.andExpect(jsonPath("$.description").value("Amazing cosmic product"))
				.andExpect(jsonPath("$.price").value(99.99)).andExpect(jsonPath("$.category").value("Electronics"))
				.andExpect(jsonPath("$.id").exists());
	}

	@Test
	void fullCrudScenario_ShouldWorkCorrectly() throws Exception {
		String productJson = objectMapper.writeValueAsString(validProductRequest);
		String createResponse = mockMvc
				.perform(post(baseUrl).contentType(MediaType.APPLICATION_JSON).content(productJson))
				.andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();

		String productId = objectMapper.readTree(createResponse).get("id").asText();

		mockMvc.perform(get(baseUrl + "/" + productId)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(productId));

		ProductRequest updateRequest = new ProductRequest("Galaxy Updated", "Updated desc", BigDecimal.valueOf(149.99),
				"Updated Cat");
		String updateJson = objectMapper.writeValueAsString(updateRequest);

		mockMvc.perform(put(baseUrl + "/" + productId).contentType(MediaType.APPLICATION_JSON).content(updateJson))
				.andExpect(status().isOk()).andExpect(jsonPath("$.name").value("Galaxy Updated"));

		mockMvc.perform(delete(baseUrl + "/" + productId)).andExpect(status().isNoContent());

		mockMvc.perform(get(baseUrl + "/" + productId)).andExpect(status().isNotFound());
	}
}
