package org.example.productcatalogservice_july2024.services;

import org.example.productcatalogservice_july2024.dtos.UserDto;
import org.example.productcatalogservice_july2024.models.Product;
import org.example.productcatalogservice_july2024.repos.ProductRepo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StorageProductServiceTest {

    @Mock
    private ProductRepo productRepo;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private StorageProductService productService;

    @Test
    @DisplayName("Get product by ID returns product when found")
    void testGetProductById_WhenProductExists_ReturnsProduct() {
        // Arrange
        Long productId = 1L;
        Product product = new Product();
        product.setId(productId);
        product.setName("Test Product");
        product.setPrice(100.0);

        when(productRepo.findById(productId)).thenReturn(Optional.of(product));

        // Act
        Product result = productService.getProductById(productId);

        // Assert
        assertNotNull(result);
        assertEquals(productId, result.getId());
        assertEquals("Test Product", result.getName());
        assertEquals(100.0, result.getPrice());

        verify(productRepo).findById(productId);
    }

    @Test
    @DisplayName("Get product by ID returns null when not found")
    void testGetProductById_WhenProductNotExists_ReturnsNull() {
        // Arrange
        Long productId = 999L;
        when(productRepo.findById(productId)).thenReturn(Optional.empty());

        // Act
        Product result = productService.getProductById(productId);

        // Assert
        assertNull(result);
        verify(productRepo).findById(productId);
    }

    @Test
    @DisplayName("Create product saves product to repository")
    void testCreateProduct_SavesProductToRepository() {
        // Arrange
        Product product = new Product();
        product.setName("New Product");
        product.setPrice(200.0);

        Product savedProduct = new Product();
        savedProduct.setId(1L);
        savedProduct.setName("New Product");
        savedProduct.setPrice(200.0);

        when(productRepo.save(product)).thenReturn(savedProduct);

        // Act
        Product result = productService.createProduct(product);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("New Product", result.getName());
        assertEquals(200.0, result.getPrice());

        verify(productRepo).save(product);
    }

    @Test
    @DisplayName("Get all products returns list of products")
    void testGetAllProducts_ReturnsAllProducts() {
        // Arrange
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Product 1");

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Product 2");

        List<Product> productList = Arrays.asList(product1, product2);
        when(productRepo.findAll()).thenReturn(productList);

        // Act
        List<Product> result = productService.getAllProducts();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Product 1", result.get(0).getName());
        assertEquals("Product 2", result.get(1).getName());

        verify(productRepo).findAll();
    }

    @Test
    @DisplayName("Replace product returns null as per implementation")
    void testReplaceProduct_ReturnsNull() {
        // Arrange
        Product product = new Product();
        product.setName("Updated Product");
        Long productId = 1L;

        // Act
        Product result = productService.replaceProduct(product, productId);

        // Assert
        assertNull(result);
    }

    @Test
    @DisplayName("Get product based on user role returns product when user exists")
    void testGetProductBasedOnUserRole_WhenUserExists_ReturnsProduct() {
        // Arrange
        Long userId = 1L;
        Long productId = 2L;

        Product product = new Product();
        product.setId(productId);
        product.setName("Role-specific Product");

        UserDto userDto = new UserDto();
        userDto.setEmail("user@example.com"); // Using email instead of id since UserDto has email not id

        when(productRepo.findById(productId)).thenReturn(Optional.of(product));
        when(restTemplate.getForEntity(eq("http://userservice/user/{userId}"), eq(UserDto.class), eq(userId)))
                .thenReturn(ResponseEntity.ok(userDto));

        // Act
        Product result = productService.getProductBasedOnUserRole(userId, productId);

        // Assert
        assertNotNull(result);
        assertEquals(productId, result.getId());
        assertEquals("Role-specific Product", result.getName());

        verify(productRepo).findById(productId);
        verify(restTemplate).getForEntity(anyString(), any(), eq(userId));
    }

    @Test
    @DisplayName("Get product based on user role returns null when user does not exist")
    void testGetProductBasedOnUserRole_WhenUserNotExists_ReturnsNull() {
        // Arrange
        Long userId = 999L;
        Long productId = 2L;

        Product product = new Product();
        product.setId(productId);
        product.setName("Role-specific Product");

        when(productRepo.findById(productId)).thenReturn(Optional.of(product));
        // Create a response with null body to simulate user not found
        ResponseEntity<UserDto> response = ResponseEntity.ok(null);
        when(restTemplate.getForEntity(eq("http://userservice/user/{userId}"), eq(UserDto.class), eq(userId)))
                .thenReturn(response);

        // Act
        Product result = productService.getProductBasedOnUserRole(userId, productId);

        // Assert
        assertNull(result);

        verify(productRepo).findById(productId);
        verify(restTemplate).getForEntity(anyString(), any(), eq(userId));
    }
}
