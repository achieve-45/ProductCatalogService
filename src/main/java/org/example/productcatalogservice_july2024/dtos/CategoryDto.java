package org.example.productcatalogservice_july2024.dtos;

import lombok.Getter;
import lombok.Setter;
import org.example.productcatalogservice_july2024.models.Product;

import java.util.List;

/**
 * CategoryDto is a Data Transfer Object for Category entities.
 * It includes category details such as id, name, and description.
 */
@Setter
@Getter
public class CategoryDto {
    /**
     * The unique identifier of the category.
     */
    private Long id;

    /**
     * The name of the category.
     */
    private String name;

    /**
     * The description of the category.
     */
    private String description;
}