package com.Api.EMS.utils;

import com.Api.EMS.dto.AuthResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ResponseUtil {

    public <T> ResponseEntity<T> createResponse(T body, HttpStatus status) {
        return new ResponseEntity<>(body, status);
    }

    public <T> ResponseEntity<T> createSuccessResponse(T body) {
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    public ResponseEntity<AuthResponse> createErrorResponse(AuthResponse authResponse, HttpStatus status) {
        return new ResponseEntity<>(authResponse, status);
    }

}
