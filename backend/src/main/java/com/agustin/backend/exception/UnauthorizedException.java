package com.agustin.backend.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends AppException {
    public UnauthorizedException(String action) {
        super("UNAUTHORIZED",
              "Not allowed to: " + action,
              HttpStatus.FORBIDDEN);
    }
}