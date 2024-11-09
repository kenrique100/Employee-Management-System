package com.Api.EMS.utils;

import com.Api.EMS.dto.AuthRequest;
import com.Api.EMS.dto.AuthResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class AuthValidationUtil {

    public static ResponseEntity<AuthResponse> validateAdminRole(AuthRequest authRequest) {
        List<String> roles = authRequest.getRoles();
        boolean isAdmin = roles != null && roles.contains("ADMIN");

        return isAdmin ? null : ResponseUtil.createErrorResponse(
                new AuthResponse("Only admins can register"), HttpStatus.FORBIDDEN);
    }
}
