package org.example.productcatalogservice_july2024.controllers;


import org.example.productcatalogservice_july2024.dtos.CategoryDto;
import org.example.productcatalogservice_july2024.dtos.ProductDto;
import org.example.productcatalogservice_july2024.dtos.SearchRequestDto;
import org.example.productcatalogservice_july2024.models.Product;
import org.example.productcatalogservice_july2024.services.ISearchService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/search")
@RestController
public class SearchController {

    private ISearchService searchService;

    @PostMapping
    public List<ProductDto> searchProducts(@RequestBody SearchRequestDto searchRequest) {
        List<Product> products = searchService.searchProducts(searchRequest.getQuery());
        List<ProductDto> response = new ArrayList<>();
        for(Product product : products) {
            response.add(getProductDto(product));
        }
        return response;
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
