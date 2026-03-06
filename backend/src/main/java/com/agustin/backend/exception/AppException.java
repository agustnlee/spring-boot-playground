package com.agustin.backend.exception;

import org.springframework.http.HttpStatus;

// Base for exceptions 

public abstract class AppException extends RuntimeException {

    private final String errorCode;
    private final HttpStatus status;

    public AppException(String errorCode, String message, HttpStatus status) {
        super(message); 
        this.errorCode = errorCode;
        this.status    = status;
    }

    public String getErrorCode() { return errorCode; } // Ex. USRE_NOT_FOUND for frontend logic
    public HttpStatus getStatus() { return status; }
}