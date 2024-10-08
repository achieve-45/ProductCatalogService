package org.example.productcatalogservice_july2024.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.example.productcatalogservice_july2024.models.Category;

/**
 * ProductDto is a Data Transfer Object for Product entities.
 * It includes product details such as id, name, description, price, imageUrl, and category.
 */
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDto {
    /**
     * The unique identifier of the product.
     */
    private Long id;

    /**
     * The name of the product.
     */
    private String name;

    /**
     * The description of the product.
     */
    private String description;

    /**
     * The price of the product.
     */
    private Double price;

    /**
     * The URL of the product's image.
     */
    private String imageUrl;

    /**
     * The category of the product.
     */
    private CategoryDto category;
}
