package org.example.productcatalogservice_july2024.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> home() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Product Catalog Service API is running");
        response.put("endpoints", new String[] {
            "/products - Get all products",
            "/products/{id} - Get product by ID"
        });

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
