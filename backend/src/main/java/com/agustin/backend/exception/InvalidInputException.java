package com.agustin.backend.exception;

import org.springframework.http.HttpStatus;

public class InvalidInputException extends AppException {
    public InvalidInputException(String message) {
        super("INVALID_INPUT",
              message,
              HttpStatus.BAD_REQUEST);
    }
}