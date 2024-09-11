package org.example.productcatalogservice_july2024.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TestController is a simple REST controller that provides a test endpoint.
 */
@RestController
@RequestMapping("/test")
public class TestController {

    /**
     * Handles GET requests to /test/{id}.
     *
     * @param id the ID provided in the path variable
     * @return a welcome message if the ID is valid
     * @throws IllegalArgumentException if the ID is 0
     */
    @GetMapping("{id}")
    public String getMessage(@PathVariable Long id) {
        if(id == 0) {
            throw new IllegalArgumentException("id is bad");
        }
        return "Welcome to Project Module";
    }
}
