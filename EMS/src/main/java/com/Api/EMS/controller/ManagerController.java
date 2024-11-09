package com.Api.EMS.controller;

import com.Api.EMS.dto.UserDTO;
import com.Api.EMS.model.User;
import com.Api.EMS.service.ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/manager")
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerService managerService;

    // Utility method to handle Mono responses
    private <T> Mono<ResponseEntity<T>> handleMonoResponse(Mono<T> monoResponse) {
        return monoResponse
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/user")
    public Mono<ResponseEntity<User>> createUser(@RequestBody UserDTO<String> userDTO) {
        return handleMonoResponse(managerService.createUser(userDTO));
    }

    @PutMapping("/user/{id}")
    public Mono<ResponseEntity<User>> updateUser(@PathVariable String id, @RequestBody UserDTO<String> userDTO) {
        return handleMonoResponse(managerService.updateUser(id, userDTO));
    }

    @DeleteMapping("/user/{id}")
    public Mono<ResponseEntity<Void>> deleteUser(@PathVariable String id) {
        return managerService.deleteUser(id)
                .thenReturn(ResponseEntity.ok().<Void>build())  // Return OK status on success
                .defaultIfEmpty(ResponseEntity.notFound().build()); // Return NOT FOUND if no user is deleted
    }

    @GetMapping("/users")
    public Flux<ResponseEntity<User>> getAllUsers() {
        return managerService.getAllUsers()
                .map(ResponseEntity::ok);  // Return a list of users with OK status
    }

    @GetMapping("/user/{id}")
    public Mono<ResponseEntity<User>> findUserById(@PathVariable String id) {
        return handleMonoResponse(managerService.findUserById(id));
    }
}
