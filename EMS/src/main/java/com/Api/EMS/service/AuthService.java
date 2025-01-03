// AuthService.java
package com.Api.EMS.service;

import com.Api.EMS.dto.AuthRequest;
import com.Api.EMS.dto.AuthResponse;
import reactor.core.publisher.Mono;

public interface AuthService {
    Mono<AuthResponse> login(AuthRequest authRequest);
    Mono<AuthResponse> signup(AuthRequest authRequest);
}
