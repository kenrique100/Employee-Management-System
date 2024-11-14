package com.Api.EMS.service;

import com.Api.EMS.dto.AuthRequest;
import com.Api.EMS.dto.AuthResponse;
import com.Api.EMS.dto.UserDTO;
import com.Api.EMS.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService<T extends User> {
    Mono<AuthResponse> signup(AuthRequest authRequest);
    Mono<T> createUser(UserDTO<String> userDTO);
    Mono<T> updateUser(String id, UserDTO<String> userDTO);
    Mono<Boolean> deleteUser(String id);
    Flux<T> getAllUsers();
    Mono<T> getUserById(String id);
}
