package com.Api.EMS.controller;

import com.Api.EMS.dto.AuthRequest;
import com.Api.EMS.dto.AuthResponse;
import com.Api.EMS.dto.UserDTO;
import com.Api.EMS.model.User;
import com.Api.EMS.service.UserService;
import com.Api.EMS.utils.AuthValidationUtil;
import com.Api.EMS.utils.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService<User> userService;

    @PostMapping("/signup")
    public Mono<ResponseEntity<AuthResponse>> signupAdmin(@RequestBody AuthRequest authRequest) {
        return AuthValidationUtil.validateAdminRole(authRequest)
                .flatMap(validRequest ->
                        userService.signup(validRequest)
                                .flatMap(ResponseUtil::handleMonoResponse)
                )
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new AuthResponse("Invalid admin role", null, null)));
    }

    @PostMapping("/user")
    public Mono<ResponseEntity<User>> createUser(@RequestBody UserDTO<String> userDTO) {
        return userService.createUser(userDTO)
                .flatMap(ResponseUtil::createSuccessResponse)
                .onErrorResume(e -> ResponseUtil.createErrorResponse(HttpStatus.BAD_REQUEST, User.class));
    }

    @GetMapping("/users")
    public Flux<ResponseEntity<User>> getAllUsers() {
        return userService.getAllUsers()
                .flatMap(ResponseUtil::createSuccessResponse);
    }

    @GetMapping("/user/{id}")
    public Mono<ResponseEntity<User>> getUserById(@PathVariable String id) {
        return userService.getUserById(id)
                .flatMap(ResponseUtil::createSuccessResponse)
                .switchIfEmpty(ResponseUtil.createNotFoundResponse(User.class));
    }

    @PutMapping("/user/{id}")
    public Mono<ResponseEntity<User>> updateUser(@PathVariable String id, @RequestBody UserDTO<String> userDTO) {
        return userService.updateUser(id, userDTO)
                .flatMap(ResponseUtil::createSuccessResponse)
                .switchIfEmpty(ResponseUtil.createNotFoundResponse(User.class));
    }

    @DeleteMapping("/user/{id}")
    public Mono<ResponseEntity<Void>> deleteUser(@PathVariable String id) {
        return userService.deleteUser(id)
                .flatMap(deleted -> deleted
                        ? ResponseUtil.createSuccessResponse(null)
                        : ResponseUtil.createNotFoundResponse(Void.class));
    }
}
