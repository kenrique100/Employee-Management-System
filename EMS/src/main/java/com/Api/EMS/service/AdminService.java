package com.Api.EMS.service;

import com.Api.EMS.dto.AuthRequest;
import com.Api.EMS.dto.AuthResponse;
import com.Api.EMS.dto.UserDTO;
import com.Api.EMS.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AdminService {
    Mono<AuthResponse> signup(AuthRequest authRequest);
    Mono<User> createUser(UserDTO<String> userDTO);
    Mono<User> updateUser(String id, UserDTO<String> userDTO);
    Mono<Boolean> deleteUser(String id);
    Flux<User> getAllUsers();
    Mono<User> findUserById(String id);
}

