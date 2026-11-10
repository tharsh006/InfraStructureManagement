package com.inframanagement.backend.auth.dto;

public record AuthResponse(
        String token,
        String tokenType,
        String email,
        String role
) {
}