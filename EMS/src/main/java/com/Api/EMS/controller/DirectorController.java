package com.Api.EMS.controller;

import com.Api.EMS.dto.UserDTO;
import com.Api.EMS.model.User;
import com.Api.EMS.service.DirectorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/director")
@RequiredArgsConstructor
public class DirectorController {

    private final DirectorService directorService;

    private <T> Mono<ResponseEntity<T>> handleMonoResponse(Mono<T> monoResponse) {
        return monoResponse
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/user")
    public Mono<ResponseEntity<User>> createUser(@RequestBody UserDTO<String> userDTO) {
        return handleMonoResponse(directorService.createUser(userDTO));
    }

    @PutMapping("/user/{id}")
    public Mono<ResponseEntity<User>> updateUser(@PathVariable String id, @RequestBody UserDTO<String> userDTO) {
        return handleMonoResponse(directorService.updateUser(id, userDTO));
    }

    @DeleteMapping("/user/{id}")
    public Mono<ResponseEntity<Void>> deleteUser(@PathVariable String id) {
        return directorService.deleteUser(id)
                .thenReturn(ResponseEntity.ok().<Void>build())
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/users")
    public Flux<ResponseEntity<User>> getAllUsers() {
        return directorService.getAllUsers()
                .map(ResponseEntity::ok);
    }

    @GetMapping("/user/{id}")
    public Mono<ResponseEntity<User>> findUserById(@PathVariable String id) {
        return handleMonoResponse(directorService.findUserById(id));
    }
}
