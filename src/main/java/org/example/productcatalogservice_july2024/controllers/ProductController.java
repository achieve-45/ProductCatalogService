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

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    @Qualifier("sps")
    private IProductService productService;

    @GetMapping
    public List<ProductDto> getProducts() {
        List<ProductDto> response = new ArrayList<>();
        List<Product> products = productService.getAllProducts();
        for(Product product : products) {
            response.add(getProductDto(product));
        }

        return response;
     }


     @GetMapping("{userId}/{productId}")
     public ResponseEntity<ProductDto> getProductBasedOnUserRole(@PathVariable("userId") Long userId, @PathVariable("productId") Long productId) {
        Product product = productService.getProductBasedOnUserRole(userId, productId);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(getProductDto(product));
     }

    @GetMapping("{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable("id") Long productId) {
        try {
            if (productId == 0) {
                throw new IllegalArgumentException("ProductId is invalid");
            } else if(productId < 0) {
                throw new IllegalArgumentException("Are you crazy ?");
            }

            Product product = productService.getProductById(productId);
            if (product == null) {
                return ResponseEntity.notFound().build();
            }

            ProductDto productDto = getProductDto(product);
            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.add("called By", "Anurag Khanna");
            return new ResponseEntity<>(productDto, headers, HttpStatus.OK);
        }catch (IllegalArgumentException exception) {
            throw exception;
        }
    }


    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto)
    {
        Product product = getProduct(productDto);
        Product result = productService.createProduct(product);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(getProductDto(result));
    }

    @PutMapping("{id}")
    public ResponseEntity<ProductDto> replaceProduct(@PathVariable Long id, @RequestBody ProductDto productDto) {
      Product input = getProduct(productDto);
      Product product = productService.replaceProduct(input,id);

      if (product == null) {
          return ResponseEntity.notFound().build();
      }

      return ResponseEntity.ok(getProductDto(product));
    }

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
