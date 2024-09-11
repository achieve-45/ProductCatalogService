package org.example.productcatalogservice_july2024.controllers;

import org.example.productcatalogservice_july2024.dtos.CategoryDto;
import org.example.productcatalogservice_july2024.dtos.ProductDto;
import org.example.productcatalogservice_july2024.models.Category;
import org.example.productcatalogservice_july2024.models.Product;
import org.example.productcatalogservice_july2024.services.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * ProductController is a REST controller that handles requests related to products.
 */
@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    //@Qualifier("sps")
    private IProductService productService;

    /**
     * Retrieves a list of all products.
     *
     * @return a list of ProductDto objects
     */
    @GetMapping
    public List<ProductDto> getProducts() {
        List<ProductDto> response = new ArrayList<>();
        List<Product> products = productService.getAllProducts();
        for(Product product : products) {
            response.add(getProductDto(product));
        }

        return response;
    }

    /**
     * Retrieves a product by its ID.
     *
     * @param productId the ID of the product to retrieve
     * @return a ResponseEntity containing the ProductDto and HTTP status
     * @throws IllegalArgumentException if the productId is invalid
     */
    @GetMapping("{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable("id") Long productId) {
        try {
            if (productId == 0) {
                throw new IllegalArgumentException("ProductId is invalid");
            } else if(productId < 0) {
                throw new IllegalArgumentException("Are you crazy ?");
            }

            //productId++;

            Product product = productService.getProductById(productId);
            ProductDto productDto = getProductDto(product);
            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.add("called By", "Anurag Khanna");
            return new ResponseEntity<>(productDto, headers, HttpStatus.OK);
        }catch (IllegalArgumentException exception) {
            throw exception;
        }
    }

    /**
     * Creates a new product.
     *
     * @param productDto the ProductDto object containing product details
     * @return the created ProductDto
     */
    @PostMapping
    public ProductDto createProduct(@RequestBody ProductDto productDto)
    {
        Product product = getProduct(productDto);
        Product result = productService.createProduct(product);
        return getProductDto(result);
    }

    /**
     * Replaces an existing product with new details.
     *
     * @param id the ID of the product to replace
     * @param productDto the ProductDto object containing new product details
     * @return the updated ProductDto
     */
    @PutMapping("{id}")
    public ProductDto replaceProduct(@PathVariable Long id, @RequestBody ProductDto productDto) {
        Product input = getProduct(productDto);
        Product product = productService.replaceProduct(input,id);
        return getProductDto(product);
    }

    /**
     * Converts a ProductDto to a Product entity.
     *
     * @param productDto the ProductDto object
     * @return the Product entity
     */
    private Product getProduct(ProductDto productDto) {
        Product product = new Product();
        product.setId(productDto.getId());
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setImageUrl(productDto.getImageUrl());
        product.setDescription(productDto.getDescription());
        if(productDto.getCategory() != null) {
            Category category = new Category();
            category.setId(productDto.getCategory().getId());
            category.setName(productDto.getCategory().getName());
            product.setCategory(category);
        }
        return product;
    }

    /**
     * Converts a Product entity to a ProductDto.
     *
     * @param product the Product entity
     * @return the ProductDto object
     */
    private ProductDto getProductDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setPrice(product.getPrice());
        productDto.setImageUrl(product.getImageUrl());
        productDto.setDescription(product.getDescription());
        if(product.getCategory() != null) {
            CategoryDto category = new CategoryDto();
            category.setId(product.getCategory().getId());
            category.setName(product.getCategory().getName());
            productDto.setCategory(category);
        }
        return productDto;
    }
}
