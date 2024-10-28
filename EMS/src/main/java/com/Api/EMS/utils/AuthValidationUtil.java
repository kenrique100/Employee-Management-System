package com.Api.EMS.utils;

import com.Api.EMS.dto.AuthRequest;
import com.Api.EMS.dto.AuthResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.List;

public class AuthValidationUtil {

    public static Mono<ResponseEntity<AuthResponse>> validateAdminRole(AuthRequest authRequest, ResponseUtil responseUtil) {
        List<String> roles = authRequest.getRoles();
        if (roles == null || !roles.contains("ADMIN")) {
            return Mono.just(responseUtil.createErrorResponse(
                    new AuthResponse("Only admins can register"), HttpStatus.FORBIDDEN));
        }
        return Mono.empty();
    }
}
