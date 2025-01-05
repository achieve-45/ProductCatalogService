package org.example.productcatalogservice_july2024.services;

import org.example.productcatalogservice_july2024.models.Product;
import org.example.productcatalogservice_july2024.repos.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JpaSearchService implements ISearchService{

    @Autowired
    private ProductRepo productRepo;

    public List<Product> searchProducts(String query) {
        return productRepo.findProductByName(query);

    }

}
