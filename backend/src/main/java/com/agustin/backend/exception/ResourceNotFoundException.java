package com.agustin.backend.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends AppException {
    public ResourceNotFoundException(String resource, Long id) {
        super("NOT_FOUND",
              resource + " not found with id: " + id,
              HttpStatus.NOT_FOUND);
    }
}