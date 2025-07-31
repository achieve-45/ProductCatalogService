package org.example.productcatalogservice_july2024.services;

import org.example.productcatalogservice_july2024.dtos.UserDto;
import org.example.productcatalogservice_july2024.models.Product;
import org.example.productcatalogservice_july2024.repos.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service("sps")
//@Primary
public class StorageProductService implements IProductService {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Product getProductById(Long id) {
        Optional<Product> optionalProduct = productRepo.findById(id);

        if(optionalProduct.isPresent()) {
            return optionalProduct.get();
        }

        return null;
    }

    @Override
    public Product createProduct(Product product) {
       return productRepo.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepo.findAll();
       // return null;
    }

    @Override
    public Product replaceProduct(Product product, Long id) {
        return null;
    }

    @Override
    public Product getProductBasedOnUserRole(Long userId, Long productId) {
        Optional<Product> productOptional = productRepo.findById(productId);
        if (!productOptional.isPresent()) {
            return null;
        }

        Product product = productOptional.get();
        try {
            UserDto userDto = restTemplate.getForEntity("http://userservice/user/{userId}", UserDto.class, userId).getBody();
            if (userDto != null) {
                System.out.println("received user");
                return product;
            } else {
                return null;
            }
        } catch (Exception e) {
            // Log the exception
            System.err.println("Error calling user service: " + e.getMessage());
            return null;
        }
    }
}
