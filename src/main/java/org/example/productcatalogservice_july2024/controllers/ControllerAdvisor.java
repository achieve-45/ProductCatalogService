package org.example.productcatalogservice_july2024.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * ControllerAdvisor is a global exception handler for REST controllers.
 */
@RestControllerAdvice
public class ControllerAdvisor {

    /**
     * Handles IllegalArgumentException and NullPointerException exceptions.
     *
     * @param exception the exception that was thrown
     * @return a ResponseEntity containing the exception message and HTTP status BAD_REQUEST
     */
    @ExceptionHandler({IllegalArgumentException.class, NullPointerException.class})
    public ResponseEntity<String> handleExceptions(Exception exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
