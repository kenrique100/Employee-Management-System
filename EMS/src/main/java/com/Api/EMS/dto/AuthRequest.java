package com.Api.EMS.dto;

import lombok.Data;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class AuthRequest {
    @NotEmpty(message = "Email is required")
    private String email;

    @NotEmpty(message = "Username is required")
    private String username;

    @NotEmpty(message = "Password is required")
    private String password;

    @NotEmpty(message = "Roles are required")
    private List<String> roles;
}
