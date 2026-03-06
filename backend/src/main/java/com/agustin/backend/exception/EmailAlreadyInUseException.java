
package com.agustin.backend.exception;

import org.springframework.http.HttpStatus;

public class EmailAlreadyInUseException extends AppException {
    public EmailAlreadyInUseException(String email) {
        super("EMAIL_IN_USE",
              "Email already registered: " + email,
              HttpStatus.CONFLICT);
    }
} 
