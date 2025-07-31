package org.example.productcatalogservice_july2024.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.productcatalogservice_july2024.dtos.CategoryDto;
import org.example.productcatalogservice_july2024.dtos.ProductDto;
import org.example.productcatalogservice_july2024.models.Category;
import org.example.productcatalogservice_july2024.models.Product;
import org.example.productcatalogservice_july2024.services.IProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class ProductControllerMvcTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IProductService productService;

    //object <-> json <-> string
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /products returns 200 status")
    public void Test_GetAllProductsAPI_TestsStatusOnly() throws Exception {
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /products returns products with content and 200 status")
    public void Test_GetAllProductsAPI_TestsContentAndStatus() throws Exception {
        //Arrange
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Iphone12");
        product1.setPrice(999.99);

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Samsung Galaxy");
        product2.setPrice(799.99);

        List<Product> products = Arrays.asList(product1, product2);

        when(productService.getAllProducts()).thenReturn(products);

        //Act & Assert
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Iphone12"))
                .andExpect(jsonPath("$[0].price").value(999.99))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Samsung Galaxy"));

        verify(productService).getAllProducts();
    }

    @Test
    @DisplayName("GET /products/{id} returns specific product with 200 status")
    public void Test_GetProductById_ReturnsProduct() throws Exception {
        // Arrange
        Long productId = 5L;

        Product product = new Product();
        product.setId(productId);
        product.setName("Test Product");
        product.setPrice(199.99);
        product.setDescription("Test Description");

        Category category = new Category();
        category.setId(1L);
        category.setName("Electronics");
        product.setCategory(category);

        when(productService.getProductById(productId)).thenReturn(product);

        // Act & Assert
        mockMvc.perform(get("/products/{id}", productId))
                .andExpect(status().isOk())
                .andExpect(header().string("called By", "Anurag Khanna"))
                .andExpect(jsonPath("$.id").value(productId))
                .andExpect(jsonPath("$.name").value("Test Product"))
                .andExpect(jsonPath("$.price").value(199.99))
                .andExpect(jsonPath("$.description").value("Test Description"))
                .andExpect(jsonPath("$.category.id").value(1))
                .andExpect(jsonPath("$.category.name").value("Electronics"));

        verify(productService).getProductById(productId);
    }

    @Test
    @DisplayName("GET /products/{id} with zero ID returns 400 Bad Request")
    public void Test_GetProductById_WithZeroId_ReturnsBadRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/products/{id}", 0))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /products/{id} with negative ID returns 400 Bad Request")
    public void Test_GetProductById_WithNegativeId_ReturnsBadRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/products/{id}", -1))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /products/{userId}/{productId} returns product based on user role")
    public void Test_GetProductBasedOnUserRole_ReturnsProduct() throws Exception {
        // Arrange
        Long userId = 10L;
        Long productId = 5L;

        Product product = new Product();
        product.setId(productId);
        product.setName("User-specific Product");
        product.setPrice(299.99);

        when(productService.getProductBasedOnUserRole(userId, productId)).thenReturn(product);

        // Act & Assert
        mockMvc.perform(get("/products/{userId}/{productId}", userId, productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productId))
                .andExpect(jsonPath("$.name").value("User-specific Product"))
                .andExpect(jsonPath("$.price").value(299.99));

        verify(productService).getProductBasedOnUserRole(userId, productId);
    }

    @Test
    @DisplayName("POST /products creates a new product")
    public void Test_CreateProduct_ReturnsCreatedProduct() throws Exception {
        // Arrange
        ProductDto inputDto = new ProductDto();
        inputDto.setName("New Product");
        inputDto.setPrice(399.99);
        inputDto.setDescription("Brand new product");

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(3L);
        categoryDto.setName("Gadgets");
        inputDto.setCategory(categoryDto);

        Product savedProduct = new Product();
        savedProduct.setId(100L);
        savedProduct.setName("New Product");
        savedProduct.setPrice(399.99);
        savedProduct.setDescription("Brand new product");

        Category category = new Category();
        category.setId(3L);
        category.setName("Gadgets");
        savedProduct.setCategory(category);

        when(productService.createProduct(any(Product.class))).thenReturn(savedProduct);

        // Act & Assert
        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.name").value("New Product"))
                .andExpect(jsonPath("$.price").value(399.99))
                .andExpect(jsonPath("$.description").value("Brand new product"))
                .andExpect(jsonPath("$.category.id").value(3))
                .andExpect(jsonPath("$.category.name").value("Gadgets"));

        verify(productService).createProduct(any(Product.class));
    }

    @Test
    @DisplayName("PUT /products/{id} updates existing product")
    public void Test_ReplaceProduct_ReturnsUpdatedProduct() throws Exception {
        // Arrange
        Long productId = 5L;

        ProductDto inputDto = new ProductDto();
        inputDto.setName("Updated Product");
        inputDto.setPrice(499.99);
        inputDto.setDescription("Updated description");

        Product updatedProduct = new Product();
        updatedProduct.setId(productId);
        updatedProduct.setName("Updated Product");
        updatedProduct.setPrice(499.99);
        updatedProduct.setDescription("Updated description");

        when(productService.replaceProduct(any(Product.class), eq(productId))).thenReturn(updatedProduct);

        // Act & Assert
        mockMvc.perform(put("/products/{id}", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productId))
                .andExpect(jsonPath("$.name").value("Updated Product"))
                .andExpect(jsonPath("$.price").value(499.99))
                .andExpect(jsonPath("$.description").value("Updated description"));

        verify(productService).replaceProduct(any(Product.class), eq(productId));
    }
}

//{
//    "name" : "iphone",
//        "id" : 1
//}

//$


//[{
//    "" : "",
//        "" : "",
//        },{},{}]
