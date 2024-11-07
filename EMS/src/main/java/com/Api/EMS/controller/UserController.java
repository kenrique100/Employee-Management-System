package com.Api.EMS.controller;

import com.Api.EMS.dto.UserDTO;
import com.Api.EMS.model.User;
import com.Api.EMS.service.UserService;
import com.Api.EMS.utils.PermissionUtil;
import com.Api.EMS.utils.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // Create user
    @PostMapping
    public Mono<ResponseEntity<User>> createUser(@RequestBody UserDTO userDTO) {
        return userService.createUser(userDTO)
                .map(user -> ResponseEntity.status(HttpStatus.CREATED).body(user))
                .onErrorResume(e -> ResponseUtil.createBadRequestResponse(User.class));
    }

    // Get all users
    @GetMapping
    public Flux<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // Get user by ID
    @GetMapping("/{id}")
    public Mono<ResponseEntity<User>> getUserById(@PathVariable String id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .switchIfEmpty(ResponseUtil.createNotFoundResponse(User.class));
    }

    // Update user
    @PutMapping("/{id}")
    public Mono<ResponseEntity<User>> updateUser(
            @PathVariable String id,
            @RequestBody UserDTO userDTO,
            Authentication authentication) {

        return PermissionUtil.hasPermission(authentication, "ADMIN", "MANAGER")
                .flatMap(hasPermission -> hasPermission
                        ? userService.updateUser(id, userDTO)
                        .map(ResponseEntity::ok)
                        .switchIfEmpty(ResponseUtil.createNotFoundResponse(User.class))
                        .onErrorResume(e -> ResponseUtil.createBadRequestResponse(User.class))
                        : ResponseUtil.createForbiddenResponse(User.class));
    }

    // Delete user
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteUser(
            @PathVariable String id,
            Authentication authentication) {

        return PermissionUtil.hasPermission(authentication, "ADMIN")
                .flatMap(hasPermission -> hasPermission
                        ? userService.deleteUser(id)
                        .then(Mono.just(ResponseEntity.ok().<Void>build()))
                        .switchIfEmpty(ResponseUtil.createNotFoundResponse(Void.class))
                        : ResponseUtil.createForbiddenResponse(Void.class));
    }
}
