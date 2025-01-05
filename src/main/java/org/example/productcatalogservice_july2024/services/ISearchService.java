package org.example.productcatalogservice_july2024.services;

import org.example.productcatalogservice_july2024.models.Product;

import java.util.List;

public interface ISearchService {
    public List<Product> searchProducts(String query);
}
