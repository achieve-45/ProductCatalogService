package org.example.productcatalogservice_july2024.controllers;

import org.example.productcatalogservice_july2024.dtos.ProductDto;
import org.example.productcatalogservice_july2024.models.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class ProductControllerFlowTest {

    @Autowired
    private ProductController productController;

    @Test
    public void Test_Create_Replace_GetProduct_WithStub_RunSuccessfully() {
        //Arrange
        ProductDto productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setName("Iphone 15");

        //Act
        ResponseEntity<ProductDto> response = productController.createProduct(productDto);
        assertNotNull(response);
        assertNotNull(response.getBody());

        ResponseEntity<ProductDto> productDtoResponseEntity = productController
                .getProductById(1L);

        productDto.setName("Iphone 16");

        ResponseEntity<ProductDto> replacedProductResponse = productController.replaceProduct(1L, productDto);
        assertNotNull(replacedProductResponse);
        assertNotNull(replacedProductResponse.getBody());

        ResponseEntity<ProductDto> productDtoResponseEntity2 = productController
                .getProductById(1L);

        //Assert
        assertEquals("Iphone 15", productDtoResponseEntity.getBody().getName());
        assertEquals("Iphone 16", productDtoResponseEntity2.getBody().getName());
    }
}
