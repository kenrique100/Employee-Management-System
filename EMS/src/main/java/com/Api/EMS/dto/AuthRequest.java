package com.Api.EMS.dto;

import lombok.Data;

import java.util.List;

@Data
public class AuthRequest {
    private String companyName;
    private String username;
    private String password;
    private List<String> roles;
}
