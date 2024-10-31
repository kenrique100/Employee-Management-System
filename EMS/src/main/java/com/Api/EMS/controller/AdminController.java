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
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final AuthService authService;

    @PostMapping("/signup")
    public Mono<ResponseEntity<AuthResponse>> signupAdmin(@RequestBody AuthRequest authRequest) {
        List<String> roles = authRequest.getRoles();
        if (roles == null || !roles.contains("ADMIN")) {
            return ResponseUtil.createForbiddenResponse(new AuthResponse("Only admins can register"));
        }
        return authService.signup(authRequest)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response))
                .onErrorResume(e -> ResponseUtil.createBadRequestResponse(new AuthResponse("Registration failed")));
    }

    @PostMapping("/user")
    public Mono<ResponseEntity<User>> createUser(@RequestBody UserDTO<String> userDTO) {
        return adminService.createUser(userDTO)
                .map(user -> ResponseEntity.status(HttpStatus.CREATED).body(user))
                .onErrorResume(e -> ResponseUtil.createBadRequestResponse(null));
    }

    @GetMapping("/users")
    public Flux<User> getAllUsers() {
        return adminService.getAllUsers();
    }

    @GetMapping("/user/{id}")
    public Mono<ResponseEntity<User>> getUserById(@PathVariable String id) {
        return adminService.getUserById(id)
                .map(ResponseEntity::ok)
                .switchIfEmpty(ResponseUtil.createNotFoundResponse());
    }

    @PutMapping("/user/{id}")
    public Mono<ResponseEntity<User>> updateUser(@PathVariable String id, @RequestBody UserDTO<String> userDTO) {
        return adminService.updateUser(id, userDTO)
                .map(ResponseEntity::ok)
                .switchIfEmpty(ResponseUtil.createNotFoundResponse())
                .onErrorResume(e -> ResponseUtil.createBadRequestResponse(null));
    }

    @DeleteMapping("/user/{id}")
    public Mono<ResponseEntity<Void>> deleteUser(@PathVariable String id) {
        return adminService.deleteUser(id)
                .then(Mono.just(ResponseEntity.ok().<Void>build()))
                .switchIfEmpty(ResponseUtil.createNotFoundResponse());
    }
}
