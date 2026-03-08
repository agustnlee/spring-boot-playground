package com.agustin.backend.controller;

import com.agustin.backend.dto.auth.AuthResponseDto;
import com.agustin.backend.dto.auth.LoginDto;
import com.agustin.backend.dto.auth.RegisterDto;
import com.agustin.backend.service.AuthService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseCookie;

import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
public class AuthController { // WRAPPING IN REQUEST ENTITY for control status code

    private final AuthService authService;

    @Value("${app.cookie.secure}")
    private boolean cookieSecure;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register( @Valid @RequestBody RegisterDto dto, HttpServletResponse response) {
        AuthResponseDto auth = authService.register(dto);
        setCookie(response, auth.getToken());
        return ResponseEntity.status(HttpStatus.CREATED).body(auth);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(
            @Valid @RequestBody LoginDto dto,
            HttpServletResponse response) {
        AuthResponseDto auth = authService.login(dto);
        setCookie(response, auth.getToken());
        return ResponseEntity.ok(auth);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        clearCookie(response);
        return ResponseEntity.ok().build();
    }


    private void setCookie(HttpServletResponse response, String token) {
        ResponseCookie cookie = ResponseCookie.from("token", token)
            .httpOnly(true)
            .secure(cookieSecure)
            .sameSite("Strict")
            .maxAge(86400)
            .path("/")
            .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    private void clearCookie(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("token", "")
            .httpOnly(true)
            .secure(cookieSecure)
            .sameSite("Strict")
            .maxAge(0)
            .path("/")
            .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

}