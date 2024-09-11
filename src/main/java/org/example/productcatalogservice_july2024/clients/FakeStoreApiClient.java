package org.example.productcatalogservice_july2024.clients;

import org.example.productcatalogservice_july2024.dtos.FakeStoreProductDto;
import org.example.productcatalogservice_july2024.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Client for interacting with the FakeStore API.
 */
@Component
public class FakeStoreApiClient {

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    /**
     * Fetches a product by its ID from the FakeStore API.
     *
     * @param id the ID of the product to fetch
     * @return the product details as a FakeStoreProductDto
     */
    public FakeStoreProductDto getProductById(Long id) {
        ResponseEntity<FakeStoreProductDto> fakeStoreProductDtoResponseEntity =
                requestForEntity("http://fakestoreapi.com/products/{id}", HttpMethod.GET, null, FakeStoreProductDto.class, id);

        return fakeStoreProductDtoResponseEntity.getBody();
    }

    /**
     * Helper method to make HTTP requests.
     *
     * @param url the URL to send the request to
     * @param httpMethod the HTTP method to use
     * @param request the request body (can be null)
     * @param responseType the type of the response body
     * @param uriVariables the URI variables to expand in the URL
     * @param <T> the type of the response body
     * @return the response entity containing the response body
     * @throws RestClientException if an error occurs during the request
     */
    private <T> ResponseEntity<T> requestForEntity(String url, HttpMethod httpMethod, @Nullable Object request, Class<T> responseType, Object... uriVariables) throws RestClientException {
        RestTemplate restTemplate = restTemplateBuilder.build();
        RequestCallback requestCallback = restTemplate.httpEntityCallback(request, responseType);
        ResponseExtractor<ResponseEntity<T>> responseExtractor = restTemplate.responseEntityExtractor(responseType);
        return restTemplate.execute(url, httpMethod, requestCallback, responseExtractor, uriVariables);
    }
}
