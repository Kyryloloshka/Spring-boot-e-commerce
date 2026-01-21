package com.example.web_market_cosmo_cats.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.hamcrest.Matchers.closeTo;

import com.example.web_market_cosmo_cats.dto.Cart.AddToCartRequest;
import com.example.web_market_cosmo_cats.dto.Product.ProductRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
@TestPropertySource(properties = "feature.shoppingCart.enabled=true")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CartControllerIT {

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
	private String baseUrl;
	private String product1Id;
	private String product2Id;

	@BeforeEach
	void setUp() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		objectMapper = new ObjectMapper();
		baseUrl = "/api/carts";

		// Create test products
		ProductRequest product1 = new ProductRequest();
		product1.setName("Star Product 1");
		product1.setDescription("Description 1");
		product1.setPrice(BigDecimal.valueOf(29.99));
		product1.setCategory("Test Category");

		String product1Json = objectMapper.writeValueAsString(product1);
		String product1Response = mockMvc
				.perform(post("/api/products").contentType(MediaType.APPLICATION_JSON).content(product1Json))
				.andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
		product1Id = objectMapper.readTree(product1Response).get("id").asText();

		ProductRequest product2 = new ProductRequest();
		product2.setName("Cosmic Product 2");
		product2.setDescription("Description 2");
		product2.setPrice(BigDecimal.valueOf(15.50));
		product2.setCategory("Test Category");

		String product2Json = objectMapper.writeValueAsString(product2);
		String product2Response = mockMvc
				.perform(post("/api/products").contentType(MediaType.APPLICATION_JSON).content(product2Json))
				.andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
		product2Id = objectMapper.readTree(product2Response).get("id").asText();
	}

	@Test
	void getAllCarts_ShouldReturnEmptyList_WhenNoCartsExist() throws Exception {
		mockMvc.perform(get(baseUrl)).andExpect(status().isOk()).andExpect(content().json("[]"));
	}

	@Test
	void createCart_ShouldReturnCreatedCart() throws Exception {
		mockMvc.perform(post(baseUrl)).andExpect(status().isCreated()).andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.items").isEmpty()).andExpect(jsonPath("$.totalPrice").value(0.0))
				.andExpect(jsonPath("$.itemCount").value(0));
	}

	@Test
	void createCartAndGetAll_ShouldWorkTogether() throws Exception {
		mockMvc.perform(post(baseUrl)).andExpect(status().isCreated());

		mockMvc.perform(get(baseUrl)).andExpect(status().isOk()).andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$.length()").value(1));
	}

	@Test
	void getCart_ShouldReturnCart_WhenCartExists() throws Exception {
		String createResponse = mockMvc.perform(post(baseUrl)).andExpect(status().isCreated()).andReturn().getResponse()
				.getContentAsString();

		String cartId = objectMapper.readTree(createResponse).get("id").asText();

		mockMvc.perform(get(baseUrl + "/" + cartId)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(cartId)).andExpect(jsonPath("$.items").isEmpty())
				.andExpect(jsonPath("$.totalPrice").value(0.0)).andExpect(jsonPath("$.itemCount").value(0));
	}

	@Test
	void getCart_ShouldReturn404_WhenCartDoesNotExist() throws Exception {
		mockMvc.perform(get(baseUrl + "/non-existent-id")).andExpect(status().isNotFound());
	}

	@Test
	void addToCart_ShouldAddProductToCart() throws Exception {
		String createResponse = mockMvc.perform(post(baseUrl)).andExpect(status().isCreated()).andReturn().getResponse()
				.getContentAsString();

		String cartId = objectMapper.readTree(createResponse).get("id").asText();

		AddToCartRequest addRequest = new AddToCartRequest(product1Id);
		String addJson = objectMapper.writeValueAsString(addRequest);

		mockMvc.perform(
				post(baseUrl + "/" + cartId + "/items").contentType(MediaType.APPLICATION_JSON).content(addJson))
				.andExpect(status().isOk()).andExpect(jsonPath("$.id").value(cartId))
				.andExpect(jsonPath("$.items").isArray()).andExpect(jsonPath("$.items.length()").value(1))
				.andExpect(jsonPath("$.totalPrice").value(29.99)).andExpect(jsonPath("$.itemCount").value(1));
	}

	@Test
	void addToCart_ShouldReturn404_WhenCartDoesNotExist() throws Exception {
		AddToCartRequest addRequest = new AddToCartRequest(product1Id);
		String addJson = objectMapper.writeValueAsString(addRequest);

		mockMvc.perform(
				post(baseUrl + "/non-existent-id/items").contentType(MediaType.APPLICATION_JSON).content(addJson))
				.andExpect(status().isNotFound());
	}

	@Test
	void clearCart_ShouldClearCartContents() throws Exception {
		String createResponse = mockMvc.perform(post(baseUrl)).andExpect(status().isCreated()).andReturn().getResponse()
				.getContentAsString();

		String cartId = objectMapper.readTree(createResponse).get("id").asText();

		AddToCartRequest addRequest = new AddToCartRequest(product1Id);
		String addJson = objectMapper.writeValueAsString(addRequest);

		mockMvc.perform(
				post(baseUrl + "/" + cartId + "/items").contentType(MediaType.APPLICATION_JSON).content(addJson))
				.andExpect(status().isOk());

		mockMvc.perform(put(baseUrl + "/" + cartId + "/clear")).andExpect(status().isNoContent());

		mockMvc.perform(get(baseUrl + "/" + cartId)).andExpect(status().isOk()).andExpect(jsonPath("$.items").isEmpty())
				.andExpect(jsonPath("$.totalPrice").value(0.0)).andExpect(jsonPath("$.itemCount").value(0));
	}

	@Test
	void deleteCart_ShouldRemoveCart() throws Exception {
		String createResponse = mockMvc.perform(post(baseUrl)).andExpect(status().isCreated()).andReturn().getResponse()
				.getContentAsString();

		String cartId = objectMapper.readTree(createResponse).get("id").asText();

		mockMvc.perform(delete(baseUrl + "/" + cartId)).andExpect(status().isNoContent());

		mockMvc.perform(get(baseUrl + "/" + cartId)).andExpect(status().isNotFound());
	}

	@Test
	void fullCartScenario_ShouldWorkCorrectly() throws Exception {
		// Create cart
		String createResponse = mockMvc.perform(post(baseUrl)).andExpect(status().isCreated()).andReturn().getResponse()
				.getContentAsString();

		String cartId = objectMapper.readTree(createResponse).get("id").asText();

		// Add first product
		AddToCartRequest addRequest1 = new AddToCartRequest(product1Id);
		String addJson1 = objectMapper.writeValueAsString(addRequest1);

		mockMvc.perform(
				post(baseUrl + "/" + cartId + "/items").contentType(MediaType.APPLICATION_JSON).content(addJson1))
				.andExpect(status().isOk()).andExpect(jsonPath("$.totalPrice").value(29.99))
				.andExpect(jsonPath("$.itemCount").value(1));

		// Add second product
		AddToCartRequest addRequest2 = new AddToCartRequest(product2Id);
		String addJson2 = objectMapper.writeValueAsString(addRequest2);

		mockMvc.perform(
				post(baseUrl + "/" + cartId + "/items").contentType(MediaType.APPLICATION_JSON).content(addJson2))
				.andExpect(status().isOk()).andExpect(jsonPath("$.totalPrice", closeTo(45.49, 0.01)))
				.andExpect(jsonPath("$.itemCount").value(2));

		// Remove first product
		mockMvc.perform(delete(baseUrl + "/" + cartId + "/items/" + product1Id)).andExpect(status().isOk())
				.andExpect(jsonPath("$.totalPrice", closeTo(15.50, 0.01))).andExpect(jsonPath("$.itemCount").value(1));

		// Clear cart
		mockMvc.perform(put(baseUrl + "/" + cartId + "/clear")).andExpect(status().isNoContent());

		// Verify cart is empty
		mockMvc.perform(get(baseUrl + "/" + cartId)).andExpect(status().isOk()).andExpect(jsonPath("$.items").isEmpty())
				.andExpect(jsonPath("$.totalPrice").value(0.0)).andExpect(jsonPath("$.itemCount").value(0));

		// Delete cart
		mockMvc.perform(delete(baseUrl + "/" + cartId)).andExpect(status().isNoContent());

		// Verify cart is deleted
		mockMvc.perform(get(baseUrl + "/" + cartId)).andExpect(status().isNotFound());
	}
}
