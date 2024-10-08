package org.example.productcatalogservice_july2024.dtos;

import lombok.Getter;
import lombok.Setter;

/**
 * FakeStoreProductDto is a Data Transfer Object for products from a fake store.
 * It includes product details such as id, title, description, category, image, and price.
 */
@Setter
@Getter
public class FakeStoreProductDto {
    /**
     * The unique identifier of the product.
     */
    private Long id;

    /**
     * The title of the product.
     */
    private String title;

    /**
     * The description of the product.
     */
    private String description;

    /**
     * The category of the product.
     */
    private String category;

    /**
     * The URL of the product's image.
     */
    private String image;

    /**
     * The price of the product.
     */
    private Double price;
}