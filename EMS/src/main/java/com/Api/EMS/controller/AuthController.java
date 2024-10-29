package com.Api.EMS.controller;

import com.Api.EMS.dto.AuthRequest;
import com.Api.EMS.dto.AuthResponse;
import com.Api.EMS.service.AuthService;
import com.Api.EMS.utils.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final ResponseUtil responseUtil;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        try {
            AuthResponse response = authService.login(authRequest);
            return responseUtil.createSuccessResponse(response);
        } catch (Exception e) {
            return responseUtil.createErrorResponse(
                    new AuthResponse("Authentication failed"), HttpStatus.UNAUTHORIZED);
        }
    }
}
