package com.Api.EMS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String message;
    private String accessToken;
    private String refreshToken;
}
