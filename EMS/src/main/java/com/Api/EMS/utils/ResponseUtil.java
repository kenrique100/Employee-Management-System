// ResponseUtil.java
package com.Api.EMS.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ResponseUtil {

    public static <T> Mono<ResponseEntity<T>> createErrorResponse(HttpStatus status, Class<T> clazz) {
        return Mono.just(ResponseEntity.status(status).body(null));
    }

    public static <T> Mono<ResponseEntity<T>> createErrorResponse(T body, HttpStatus status) {
        return Mono.just(ResponseEntity.status(status).body(body));
    }

    public static <T> Mono<ResponseEntity<T>> createNotFoundResponse(Class<T> clazz) {
        return createErrorResponse(HttpStatus.NOT_FOUND, clazz);
    }

    public static <T> Mono<ResponseEntity<T>> createSuccessResponse(T body) {
        return Mono.just(ResponseEntity.ok(body));
    }

    // Updated method
    public static <T> Mono<ResponseEntity<T>> handleMonoResponse(T response) {
        return response != null
                ? Mono.just(ResponseEntity.ok(response))
                : Mono.empty();
    }

}
