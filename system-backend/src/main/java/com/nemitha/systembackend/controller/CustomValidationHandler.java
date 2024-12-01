package com.nemitha.systembackend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice(assignableTypes = ConfigurationController.class) // Apply to ConfigurationController
public class CustomValidationHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {

        // Map to store field errors
        Map<String, String> errors = new HashMap<>();

        // Iterate through field errors and store them in the map
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            String field = error.getField();
            String message = error.getDefaultMessage();
            errors.put(field, message);

            // Log the error message
            System.out.println("Validation error: " + field + " - " + message);
        }

        // Return the error message with bad request status
        return new ResponseEntity<>(errors.toString(), HttpStatus.BAD_REQUEST);
    }
}