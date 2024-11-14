// UserControllerBase.java
package com.Api.EMS.controller;

import com.Api.EMS.dto.UserDTO;
import com.Api.EMS.model.User;
import com.Api.EMS.service.UserService;
import com.Api.EMS.utils.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public abstract class UserControllerBase {

    protected final UserService<User> userService;

    protected UserControllerBase(UserService<User> userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    public Mono<ResponseEntity<User>> createUser(@RequestBody UserDTO<String> userDTO) {
        return userService.createUser(userDTO)
                .flatMap(ResponseUtil::createSuccessResponse)
                .onErrorResume(e -> ResponseUtil.createErrorResponse(HttpStatus.BAD_REQUEST, User.class));
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

    @GetMapping("/users")
    public Flux<ResponseEntity<User>> getAllUsers() {
        return userService.getAllUsers()
                .flatMap(ResponseUtil::createSuccessResponse);
    }

    @GetMapping("/user/{id}")
    public Mono<ResponseEntity<User>> findUserById(@PathVariable String id) {
        return userService.getUserById(id)
                .flatMap(ResponseUtil::createSuccessResponse)
                .switchIfEmpty(ResponseUtil.createNotFoundResponse(User.class));
    }
}
