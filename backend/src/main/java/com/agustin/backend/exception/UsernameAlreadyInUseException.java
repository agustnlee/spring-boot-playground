package com.agustin.backend.exception;

import org.springframework.http.HttpStatus;

public class UsernameAlreadyInUseException extends AppException {
    public UsernameAlreadyInUseException(String username) {
        super("USERNAME_IN_USE",
              "Username already taken: " + username,
              HttpStatus.CONFLICT);
    }
}