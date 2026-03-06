package com.agustin.backend.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponseDto {
    private String token;
    private Long   userId;
    private String email;
    private String username;
    private String role;
}