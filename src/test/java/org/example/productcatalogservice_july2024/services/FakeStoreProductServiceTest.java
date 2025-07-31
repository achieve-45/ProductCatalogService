package org.example.productcatalogservice_july2024.services;

import org.example.productcatalogservice_july2024.clients.FakeStoreApiClient;
import org.example.productcatalogservice_july2024.dtos.FakeStoreProductDto;
import org.example.productcatalogservice_july2024.models.Category;
import org.example.productcatalogservice_july2024.models.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FakeStoreProductServiceTest {

    @Mock
    private FakeStoreApiClient fakeStoreApiClient;

    @Mock
    private RestTemplateBuilder restTemplateBuilder;

    @Mock
    // Using raw type since the service implementation uses raw type
    private RedisTemplate redisTemplate;

    @Mock
    private HashOperations hashOperations;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private FakeStoreProductService productService;

    @BeforeEach
    void setUp() {
        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        when(restTemplateBuilder.build()).thenReturn(restTemplate);
    }

    @Test
    @DisplayName("Get product by ID returns product from cache if available")
    void testGetProductById_WhenProductInCache_ReturnsProductFromCache() {
        // Arrange
        Long productId = 1L;
        FakeStoreProductDto cachedDto = new FakeStoreProductDto();
        cachedDto.setId(productId);
        cachedDto.setTitle("Cached Product");
        cachedDto.setPrice(100.0);
        cachedDto.setCategory("Electronics");
        cachedDto.setDescription("A cached product");
        cachedDto.setImage("image-url");

        when(hashOperations.get(eq("PRODUCTS__"), eq(productId))).thenReturn(cachedDto);

        // Act
        Product result = productService.getProductById(productId);

        // Assert
        assertNotNull(result);
        assertEquals("Cached Product", result.getName());
        assertEquals(100.0, result.getPrice());
        assertEquals("A cached product", result.getDescription());
        assertEquals("image-url", result.getImageUrl());
        assertEquals("Electronics", result.getCategory().getName());

        verify(hashOperations).get(eq("PRODUCTS__"), eq(productId));
        verify(fakeStoreApiClient, never()).getProductById(anyLong());
    }

    @Test
    @DisplayName("Get product by ID retrieves from API and caches when not in cache")
    void testGetProductById_WhenProductNotInCache_ReturnsProductFromApiAndCaches() {
        // Arrange
        Long productId = 1L;
        when(hashOperations.get(eq("PRODUCTS__"), eq(productId))).thenReturn(null);

        FakeStoreProductDto apiDto = new FakeStoreProductDto();
        apiDto.setId(productId);
        apiDto.setTitle("API Product");
        apiDto.setPrice(200.0);
        apiDto.setCategory("Clothing");
        apiDto.setDescription("A product from API");
        apiDto.setImage("api-image-url");

        when(fakeStoreApiClient.getProductById(productId)).thenReturn(apiDto);

        // Act
        Product result = productService.getProductById(productId);

        // Assert
        assertNotNull(result);
        assertEquals("API Product", result.getName());
        assertEquals(200.0, result.getPrice());
        assertEquals("A product from API", result.getDescription());
        assertEquals("api-image-url", result.getImageUrl());
        assertEquals("Clothing", result.getCategory().getName());

        // Verify the interaction pattern
        verify(hashOperations).get(eq("PRODUCTS__"), eq(productId));
        verify(fakeStoreApiClient).getProductById(eq(productId));
        verify(hashOperations).put(eq("PRODUCTS__"), eq(productId), eq(apiDto));
    }

    @Test
    @DisplayName("Get product by ID returns null when API returns null")
    void testGetProductById_WhenApiReturnsNull_ReturnsNull() {
        // Arrange
        Long productId = 999L;
        when(hashOperations.get(eq("PRODUCTS__"), eq(productId))).thenReturn(null);
        when(fakeStoreApiClient.getProductById(productId)).thenReturn(null);

        // Act
        Product result = productService.getProductById(productId);

        // Assert
        assertNull(result);

        verify(hashOperations).get(eq("PRODUCTS__"), eq(productId));
        verify(fakeStoreApiClient).getProductById(eq(productId));
        verify(hashOperations, never()).put(anyString(), any(), any());
    }

    @Test
    @DisplayName("Create product returns null as per implementation")
    void testCreateProduct_ReturnsNull() {
        // Arrange
        Product product = new Product();
        product.setName("Test Product");

        // Act
        Product result = productService.createProduct(product);

        // Assert
        assertNull(result);
    }

    @Test
    @DisplayName("Get all products returns products from API")
    void testGetAllProducts_ReturnsAllProducts() {
        // Arrange
        FakeStoreProductDto[] productsArray = new FakeStoreProductDto[2];

        FakeStoreProductDto dto1 = new FakeStoreProductDto();
        dto1.setId(1L);
        dto1.setTitle("Product 1");
        dto1.setPrice(100.0);
        dto1.setCategory("Electronics");

        FakeStoreProductDto dto2 = new FakeStoreProductDto();
        dto2.setId(2L);
        dto2.setTitle("Product 2");
        dto2.setPrice(200.0);
        dto2.setCategory("Clothing");

        productsArray[0] = dto1;
        productsArray[1] = dto2;

        ResponseEntity<FakeStoreProductDto[]> responseEntity = ResponseEntity.ok(productsArray);
        when(restTemplate.getForEntity(eq("http://fakestoreapi.com/products"), eq(FakeStoreProductDto[].class)))
                .thenReturn(responseEntity);

        // Act
        List<Product> result = productService.getAllProducts();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("Product 1", result.get(0).getName());
        assertEquals(100.0, result.get(0).getPrice());
        assertEquals("Electronics", result.get(0).getCategory().getName());
        assertEquals("Product 2", result.get(1).getName());
    }

    @Test
    @DisplayName("Replace product updates product via API")
    void testReplaceProduct_UpdatesExistingProduct() {
        // Arrange
        Long productId = 1L;
        Product product = new Product();
        product.setName("Updated Product");
        product.setPrice(150.0);
        product.setDescription("Updated description");

        Category category = new Category();
        category.setName("Updated Category");
        product.setCategory(category);

        FakeStoreProductDto requestDto = new FakeStoreProductDto();
        requestDto.setTitle("Updated Product");
        requestDto.setPrice(150.0);
        requestDto.setDescription("Updated description");
        requestDto.setCategory("Updated Category");

        FakeStoreProductDto responseDto = new FakeStoreProductDto();
        responseDto.setId(productId);
        responseDto.setTitle("Updated Product");
        responseDto.setPrice(150.0);
        responseDto.setDescription("Updated description");
        responseDto.setCategory("Updated Category");

        // Mock the requestForEntity behavior
        when(restTemplate.execute(
                anyString(),
                eq(HttpMethod.PUT),
                any(),
                any(),
                eq(productId)
        )).thenReturn(ResponseEntity.ok(responseDto));

        // Act
        Product result = productService.replaceProduct(product, productId);

        // Assert
        assertNotNull(result);
        assertEquals(productId, result.getId());
        assertEquals("Updated Product", result.getName());
        assertEquals(150.0, result.getPrice());
        assertEquals("Updated description", result.getDescription());
        assertEquals("Updated Category", result.getCategory().getName());
    }

    @Test
    @DisplayName("Get product based on user role returns null as per implementation")
    void testGetProductBasedOnUserRole_ReturnsNull() {
        // Arrange
        Long userId = 1L;
        Long productId = 2L;

        // Act
        Product result = productService.getProductBasedOnUserRole(userId, productId);

        // Assert
        assertNull(result);
    }
}
