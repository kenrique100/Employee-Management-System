package com.Api.EMS.utils;

import com.Api.EMS.dto.AuthRequest;
import reactor.core.publisher.Mono;

public class AuthValidationUtil {

    public static Mono<AuthRequest> validateAdminRole(AuthRequest authRequest) {
        if (authRequest.getRoles().contains("ADMIN")) {
            return Mono.just(authRequest);
        } else {
            return Mono.empty();
        }
    }
}
