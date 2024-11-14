package com.Api.EMS.controller;

import com.Api.EMS.dto.AuthRequest;
import com.Api.EMS.dto.AuthResponse;
import com.Api.EMS.security.JwtUtil;
import com.Api.EMS.service.AuthService;
import com.Api.EMS.utils.ResponseUtil;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public Mono<ResponseEntity<AuthResponse>> login(@RequestBody AuthRequest authRequest) {
        return authService.authenticate(authRequest)
                .flatMap(ResponseUtil::createSuccessResponse)
                .switchIfEmpty(ResponseUtil.createErrorResponse(HttpStatus.UNAUTHORIZED, AuthResponse.class));
    }

    @PostMapping("/refresh")
    public Mono<ResponseEntity<?>> refreshAccessToken(@RequestParam String refreshToken) {
        try {
            String newAccessToken = jwtUtil.refreshAccessToken(refreshToken);
            return Mono.just(ResponseEntity.ok(new AuthResponse("Token refreshed successfully", newAccessToken, null)));
        } catch (JwtException e) {
            return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired refresh token."));
        }
    }

}
