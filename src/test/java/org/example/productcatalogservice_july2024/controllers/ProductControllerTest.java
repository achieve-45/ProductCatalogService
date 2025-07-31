package org.example.productcatalogservice_july2024.controllers;


import org.example.productcatalogservice_july2024.dtos.ProductDto;
import org.example.productcatalogservice_july2024.models.Category;
import org.example.productcatalogservice_july2024.models.Product;
import org.example.productcatalogservice_july2024.services.IProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
class ProductControllerTest {

    @Autowired
    private ProductController productController;

    @MockBean
    private IProductService productService;

    @Captor
    private ArgumentCaptor<Long> idCaptor;

    @Captor
    private ArgumentCaptor<Product> productCaptor;

    @Test
    @DisplayName("Get product by ID with valid ID returns product successfully")
    public void Test_GetProductById_WithValidId_ReturnsProductSuccessfully() {
        //Arrange
        Long productId = 9999999999L;
        Product product = new Product();
        product.setId(productId);
        product.setName("Iphone12");
        product.setPrice(100000D);
        Category category = new Category();
        category.setId(2L);
        category.setName("iPHONES");
        product.setCategory(category);
        when(productService.getProductById(productId)).thenReturn(product);

        //Act
        ResponseEntity<ProductDto> response = productController.getProductById(productId);

        //Assert
        assertNotNull(response);
        assertEquals("Iphone12", response.getBody().getName());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Anurag Khanna", response.getHeaders().getFirst("called By"));
        verify(productService).getProductById(productId);
    }

    @Test
    @DisplayName("Get product by ID with zero ID throws IllegalArgumentException")
    public void Test_GetProductById_WithZeroId_ThrowsIllegalArgumentException() {
        // Arrange
        Long productId = 0L;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> productController.getProductById(productId)
        );
        assertEquals("ProductId is invalid", exception.getMessage());
        verify(productService, never()).getProductById(anyLong());
    }

    @Test
    @DisplayName("Get product by ID with negative ID throws IllegalArgumentException")
    public void Test_GetProductById_WithNegativeId_ThrowsIllegalArgumentException() {
        // Arrange
        Long productId = -1L;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> productController.getProductById(productId)
        );
        assertEquals("Are you crazy ?", exception.getMessage());
        verify(productService, never()).getProductById(anyLong());
    }

    @Test
    @DisplayName("Get all products returns list of products successfully")
    public void Test_GetProducts_ReturnsAllProducts() {
        // Arrange
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Product 1");
        product1.setPrice(100D);

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Product 2");
        product2.setPrice(200D);

        List<Product> products = Arrays.asList(product1, product2);
        when(productService.getAllProducts()).thenReturn(products);

        // Act
        List<ProductDto> result = productController.getProducts();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Product 1", result.get(0).getName());
        assertEquals("Product 2", result.get(1).getName());
        verify(productService).getAllProducts();
    }

    @Test
    @DisplayName("Get product based on user role returns product successfully")
    public void Test_GetProductBasedOnUserRole_ReturnsProduct() {
        // Arrange
        Long userId = 1L;
        Long productId = 2L;

        Product product = new Product();
        product.setId(productId);
        product.setName("Test Product");
        product.setPrice(150D);

        when(productService.getProductBasedOnUserRole(userId, productId)).thenReturn(product);

        // Act
        ResponseEntity<ProductDto> response = productController.getProductBasedOnUserRole(userId, productId);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals("Test Product", response.getBody().getName());
        assertEquals(150D, response.getBody().getPrice());
        verify(productService).getProductBasedOnUserRole(userId, productId);
    }

    @Test
    @DisplayName("Create product with valid data returns created product")
    public void Test_CreateProduct_WithValidData_ReturnsCreatedProduct() {
        // Arrange
        ProductDto inputDto = new ProductDto();
        inputDto.setName("New Product");
        inputDto.setPrice(299.99);
        inputDto.setDescription("Test Description");

        Product savedProduct = new Product();
        savedProduct.setId(1L);
        savedProduct.setName("New Product");
        savedProduct.setPrice(299.99);
        savedProduct.setDescription("Test Description");

        when(productService.createProduct(any(Product.class))).thenReturn(savedProduct);

        // Act
        ResponseEntity<ProductDto> response = productController.createProduct(inputDto);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals("New Product", response.getBody().getName());
        assertEquals(299.99, response.getBody().getPrice());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        verify(productService).createProduct(productCaptor.capture());
        Product capturedProduct = productCaptor.getValue();
        assertEquals("New Product", capturedProduct.getName());
        assertEquals(299.99, capturedProduct.getPrice());
    }

    @Test
    @DisplayName("Replace product updates existing product")
    public void Test_ReplaceProduct_UpdatesExistingProduct() {
        // Arrange
        Long productId = 1L;

        ProductDto inputDto = new ProductDto();
        inputDto.setName("Updated Product");
        inputDto.setPrice(399.99);

        Product updatedProduct = new Product();
        updatedProduct.setId(productId);
        updatedProduct.setName("Updated Product");
        updatedProduct.setPrice(399.99);

        when(productService.replaceProduct(any(Product.class), eq(productId))).thenReturn(updatedProduct);

        // Act
        ResponseEntity<ProductDto> response = productController.replaceProduct(productId, inputDto);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(productId, response.getBody().getId());
        assertEquals("Updated Product", response.getBody().getName());
        assertEquals(399.99, response.getBody().getPrice());
        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(productService).replaceProduct(productCaptor.capture(), eq(productId));
        Product capturedProduct = productCaptor.getValue();
        assertEquals("Updated Product", capturedProduct.getName());
        assertEquals(399.99, capturedProduct.getPrice());
    }
}