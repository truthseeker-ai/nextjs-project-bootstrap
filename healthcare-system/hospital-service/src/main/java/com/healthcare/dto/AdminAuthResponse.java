package com.healthcare.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminAuthResponse {
    private String token;
    private String tokenType = "Bearer";
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private Long hospitalId;
    private String permissions;
    private Boolean isActive;
}
