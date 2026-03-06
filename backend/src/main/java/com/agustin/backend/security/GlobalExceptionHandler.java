package com.agustin.backend.security;

import com.agustin.backend.dto.error.ErrorResponse;
import com.agustin.backend.exception.AppException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

// catching all custom excpetions. From specific to generic, with the goal to never show stack trace to client

@RestControllerAdvice
public class GlobalExceptionHandler {

    
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorResponse> handleAppException(AppException exception) {
        return ResponseEntity
            .status(exception.getStatus())
            .body(new ErrorResponse(exception.getErrorCode(), exception.getMessage()));
    }

    // catches annotation @NotBlank, @Email, @Size fails
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(e -> e.getField() + ": " + e.getDefaultMessage())
            .collect(Collectors.joining(", "));

        return ResponseEntity
            .badRequest()
            .body(new ErrorResponse("VALIDATION_ERROR", message));
    }

    // DB constraint hit
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDbConstraint(DataIntegrityViolationException exception) {
        String message = exception.getMessage();

        if (message != null && message.contains("uk_users_email")) // although can be used for unique key, this is unique contraint
            return ResponseEntity.status(409)
                .body(new ErrorResponse("EMAIL_IN_USE", "Email already registered"));

        if (message != null && message.contains("uk_users_username"))
            return ResponseEntity.status(409)
                .body(new ErrorResponse("USERNAME_IN_USE", "Username already taken"));

        return ResponseEntity.status(409)
            .body(new ErrorResponse("CONSTRAINT_VIOLATION", "Data integrity error"));
    }

    // catch-all — as to not expose stack traces
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception exception) {
        return ResponseEntity.status(500)
            .body(new ErrorResponse("INTERNAL_ERROR", "Something went wrong"));
    }
}