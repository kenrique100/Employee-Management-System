package com.Api.EMS.controller;

import com.Api.EMS.dto.AuthRequest;
import com.Api.EMS.dto.AuthResponse;
import com.Api.EMS.dto.UserDTO;
import com.Api.EMS.model.User;
import com.Api.EMS.service.AdminService;
import com.Api.EMS.service.AuthService;
import com.Api.EMS.utils.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final AuthService authService;
    private final ResponseUtil responseUtil;

    @PostMapping("/signup")
    public Mono<ResponseEntity<AuthResponse>> signupAdmin(@RequestBody AuthRequest authRequest) {
        List<String> roles = authRequest.getRoles();

        // Ensure only admins can register
        if (roles == null || !roles.contains("ADMIN")) {
            return Mono.just(responseUtil.createErrorResponse(
                    new AuthResponse("Only admins can register"), HttpStatus.FORBIDDEN));
        }

        return authService.signup(authRequest)
                .map(responseUtil::createSuccessResponse)
                .onErrorResume(e -> Mono.just(responseUtil.createErrorResponse(
                        new AuthResponse("Registration failed"), HttpStatus.BAD_REQUEST)));
    }

    @PostMapping("/user")
    public Mono<ResponseEntity<User>> createUser(@RequestBody UserDTO<String> userDTO) {
        return adminService.createUser(userDTO)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body(null)));
    }

    @GetMapping("/users")
    public Flux<User> getAllUsers() {
        return adminService.getAllUsers();
    }

    @GetMapping("/user/{id}")
    public Mono<ResponseEntity<User>> getUserById(@PathVariable String id) {
        return adminService.findUserById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/user/{id}")
    public Mono<ResponseEntity<User>> updateUser(@PathVariable String id, @RequestBody UserDTO<String> userDTO) {
        return adminService.updateUser(id, userDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body(null)));
    }

    @DeleteMapping("/user/{id}")
    public Mono<ResponseEntity<Void>> deleteUser(@PathVariable String id) {
        return adminService.deleteUser(id)
                .then(Mono.just(ResponseEntity.ok().<Void>build()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
