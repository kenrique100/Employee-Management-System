package com.Api.EMS.utils;

import com.Api.EMS.dto.AuthResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ResponseUtil {

    public static <T> Mono<ResponseEntity<T>> createErrorResponse(T body, HttpStatus status) {
        return Mono.just(ResponseEntity.status(status).body(body));
    }

    public ResponseEntity<AuthResponse> createErrorResponse(AuthResponse authResponse, HttpStatus status) {
        return new ResponseEntity<>(authResponse, status);
    }
    public static <T> Mono<ResponseEntity<T>> createBadRequestResponse(T body) {
        return createErrorResponse(body, HttpStatus.BAD_REQUEST);
    }

    public static <T> Mono<ResponseEntity<T>> createForbiddenResponse(T body) {
        return createErrorResponse(body, HttpStatus.FORBIDDEN);
    }

    public static <T> Mono<ResponseEntity<T>> createNotFoundResponse() {
        return Mono.just(ResponseEntity.notFound().build());
    }
    public <T> ResponseEntity<T> createSuccessResponse(T body) {
        return ResponseEntity.ok(body);
    }
    public static <T> Mono<ResponseEntity<T>> handleMonoResponse(Mono<T> monoResponse) {
        return monoResponse
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    public static Mono<ResponseEntity<Void>> handleVoidResponse(Mono<Void> monoResponse) {
        return monoResponse
                .thenReturn(ResponseEntity.ok().<Void>build())
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}