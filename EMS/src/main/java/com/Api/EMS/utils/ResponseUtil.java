package com.Api.EMS.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ResponseUtil {

    // General error response creator with status and type information
    public static <T> Mono<ResponseEntity<T>> createErrorResponse(HttpStatus status, Class<T> clazz) {
        return Mono.just(ResponseEntity.status(status).body(null));
    }

    public static <T> Mono<ResponseEntity<T>> createNotFoundResponse(Class<T> clazz) {
        return createErrorResponse(HttpStatus.NOT_FOUND, clazz);
    }

    public static <T> Mono<ResponseEntity<T>> createBadRequestResponse(Class<T> clazz) {
        return createErrorResponse(HttpStatus.BAD_REQUEST, clazz);
    }

    public static <T> Mono<ResponseEntity<T>> createForbiddenResponse(Class<T> clazz) {
        return createErrorResponse(HttpStatus.FORBIDDEN, clazz);
    }

    public static <T> ResponseEntity<T> createSuccessResponse(T body) {
        return ResponseEntity.ok(body);
    }
}
