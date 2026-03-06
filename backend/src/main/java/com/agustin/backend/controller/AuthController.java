package com.agustin.backend.controller;

import com.agustin.backend.dto.auth.AuthResponseDto;
import com.agustin.backend.dto.auth.LoginDto;
import com.agustin.backend.dto.auth.RegisterDto;
import com.agustin.backend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController { // WRAPPING IN REQUEST ENTITY for control status code

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@Valid @RequestBody RegisterDto dto) {
        return ResponseEntity
            .status(HttpStatus.CREATED)  // 201
            .body(authService.register(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginDto dto) {
        return ResponseEntity
            .ok(authService.login(dto));  // 200
    }
}