package com.Api.EMS.controller;

import com.Api.EMS.dto.AuthRequest;
import com.Api.EMS.dto.AuthResponse;
import com.Api.EMS.service.AuthService;
import com.Api.EMS.utils.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final ResponseUtil responseUtil;

    @PostMapping("/login")
    public Mono<ResponseEntity<AuthResponse>> login(@RequestBody AuthRequest authRequest) {
        return authService.login(authRequest)
                .map(responseUtil::createSuccessResponse)
                .defaultIfEmpty(responseUtil.createErrorResponse(
                        new AuthResponse("Authentication failed"), HttpStatus.UNAUTHORIZED));
    }

    @PostMapping("/signup")
    public Mono<ResponseEntity<AuthResponse>> signupAdmin(@RequestBody AuthRequest authRequest) {
        List<String> roles = authRequest.getRoles();
        if (roles == null || !roles.contains("ADMIN")) {
            return Mono.just(responseUtil.createErrorResponse(
                    new AuthResponse("Only admins can register"), HttpStatus.FORBIDDEN));
        }

        return authService.signup(authRequest)
                .map(responseUtil::createSuccessResponse)
                .onErrorResume(e -> Mono.just(responseUtil.createErrorResponse(
                        new AuthResponse("Registration failed"), HttpStatus.BAD_REQUEST)));
    }
}
