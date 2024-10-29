package com.Api.EMS.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ResponseUtil {

    public <T> ResponseEntity<T> createSuccessResponse(T body) {
        return ResponseEntity.ok(body);
    }

    public <T> ResponseEntity<T> createErrorResponse(T body, HttpStatus status) {
        return ResponseEntity.status(status).body(body);
    }
}
