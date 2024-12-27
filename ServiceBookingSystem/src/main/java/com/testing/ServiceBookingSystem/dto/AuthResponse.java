package com.testing.ServiceBookingSystem.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private String name;
    private String role;
    private String token;
    private String id;
}
