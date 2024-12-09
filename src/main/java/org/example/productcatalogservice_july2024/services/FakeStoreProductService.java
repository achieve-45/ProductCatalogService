package org.example.productcatalogservice_july2024.services;

import org.example.productcatalogservice_july2024.clients.FakeStoreApiClient;

import org.example.productcatalogservice_july2024.dtos.FakeStoreProductDto;
import org.example.productcatalogservice_july2024.models.Category;
import org.example.productcatalogservice_july2024.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service("fkps")
public class FakeStoreProductService implements IProductService {

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;


    @Autowired
    private FakeStoreApiClient fakeStoreApiClient;

    @Override
    public Product getProductById(Long id) {
        FakeStoreProductDto fakeStoreProductDto = fakeStoreApiClient.getProductById(id);
        if(fakeStoreProductDto != null) {
            return getProduct(fakeStoreProductDto);
        }
        return null;
    }

    /*@Override
    public Product getProductById(Long id) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<FakeStoreProductDto> fakeStoreProductDtoResponseEntity = restTemplate.getForEntity("https://fakestoreapi.com/products/{id}", FakeStoreProductDto.class,id);
        if(fakeStoreProductDtoResponseEntity.getStatusCode().is2xxSuccessful() && fakeStoreProductDtoResponseEntity.getBody() != null) {
            return getProduct(fakeStoreProductDtoResponseEntity.getBody());
        }

        return null;
    } */

    @Override
    public Product createProduct(Product product) {
        /*RestTemplate restTemplate = restTemplateBuilder.build();
        FakeStoreProductDto fakeStoreProductDto = getFakeStoreProductDto(product);
        FakeStoreProductDto create = restTemplate.postForEntity("https://fakestoreapi.com/products", fakeStoreProductDto, FakeStoreProductDto.class).getBody();
        assert create != null;
        return getProduct(create);*/
        return null;
    }

    @Override
    public List<Product> getAllProducts() {
        RestTemplate restTemplate = restTemplateBuilder.build();
        FakeStoreProductDto[] response = restTemplate.getForEntity("https://fakestoreapi.com/products", FakeStoreProductDto[].class).getBody();
        List<Product> products = new ArrayList<>();
        for(FakeStoreProductDto fakeStoreProductDto : response){
            products.add(getProduct(fakeStoreProductDto));
        }
        return products;

    }

    @Override
    public Product replaceProduct(Product product , Long id) {
        FakeStoreProductDto fakeStoreProductDto = getFakeStoreProductDto(product);
        FakeStoreProductDto fakeStoreProductDtoResponse = requestForEntity("https://fakestoreapi.com/products/{id}", HttpMethod.PUT, fakeStoreProductDto, FakeStoreProductDto.class, id).getBody();
        return getProduct(fakeStoreProductDtoResponse);
    }

    private FakeStoreProductDto getFakeStoreProductDto(Product product) {
        FakeStoreProductDto fakeStoreProductDto = new FakeStoreProductDto();
        fakeStoreProductDto.setId(product.getId());
        fakeStoreProductDto.setTitle(product.getName());
        fakeStoreProductDto.setPrice(product.getPrice());
        fakeStoreProductDto.setDescription(product.getDescription());
        fakeStoreProductDto.setImage(product.getImageUrl());
        if(product.getCategory() != null){
            fakeStoreProductDto.setCategory(product.getCategory().getName());
        }
        return fakeStoreProductDto;
    }

    private <T> ResponseEntity<T> requestForEntity(String url, HttpMethod httpMethod, @Nullable Object request, Class<T> responseType, Object... uriVariables) throws RestClientException {
        RestTemplate restTemplate = restTemplateBuilder.build();
        RequestCallback requestCallback = restTemplate.httpEntityCallback(request, responseType);
        ResponseExtractor<ResponseEntity<T>> responseExtractor = restTemplate.responseEntityExtractor(responseType);
        return restTemplate.execute(url, HttpMethod.PUT, requestCallback, responseExtractor, uriVariables);
    }

    private Product getProduct(FakeStoreProductDto fakeStoreProductDto) {
        Product product = new Product();
        product.setId(fakeStoreProductDto.getId());
        product.setName(fakeStoreProductDto.getTitle());
        product.setPrice(fakeStoreProductDto.getPrice());
        product.setDescription(fakeStoreProductDto.getDescription());
        product.setImageUrl(fakeStoreProductDto.getImage());
        Category category = new Category();
        category.setName(fakeStoreProductDto.getCategory());
        product.setCategory(category);
        return product;
    }
}
