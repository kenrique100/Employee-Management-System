package com.Api.EMS.controller;

import com.Api.EMS.dto.AuthRequest;
import com.Api.EMS.dto.AuthResponse;
import com.Api.EMS.dto.UserDTO;
import com.Api.EMS.model.User;
import com.Api.EMS.service.AdminService;
import com.Api.EMS.service.AuthService;
import com.Api.EMS.utils.AuthValidationUtil;
import com.Api.EMS.utils.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final AuthService authService;
    private final ResponseUtil responseUtil;

    // Only admin signup
    @PostMapping("/signup")
    public Mono<ResponseEntity<AuthResponse>> signupAdmin(@RequestBody AuthRequest authRequest) {
        // Validate if the user has an admin role before signing up
        return AuthValidationUtil.validateAdminRole(authRequest, responseUtil)
                .switchIfEmpty(authService.signup(authRequest)
                        .map(responseUtil::createSuccessResponse)
                        .onErrorResume(e -> Mono.just(responseUtil.createErrorResponse(
                                new AuthResponse("Admin registration failed"), HttpStatus.BAD_REQUEST))));
    }

    // Create a new user (admin only)
    @PostMapping("/user")
    public Mono<ResponseEntity<User>> createUser(@RequestBody UserDTO<String> userDTO) {
        return adminService.createUser(userDTO)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body(null)));
    }

    // Get all users (admin only)
    @GetMapping("/users")
    public Flux<User> getAllUsers() {
        return adminService.getAllUsers();
    }

    // Get a user by their ID
    @GetMapping("/user/{id}")
    public Mono<ResponseEntity<User>> getUserById(@PathVariable Long id) {
        return adminService.findUserById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    // Update a user
    @PutMapping("/user/{id}")
    public Mono<ResponseEntity<User>> updateUser(@PathVariable Long id, @RequestBody UserDTO<String> userDTO) {
        return adminService.updateUser(id, userDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body(null)));
    }

    // Delete a user by their ID
    @DeleteMapping("/user/{id}")
    public Mono<ResponseEntity<Void>> deleteUser(@PathVariable Long id) {
        return adminService.deleteUser(id)
                .then(Mono.just(ResponseEntity.ok().<Void>build()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
