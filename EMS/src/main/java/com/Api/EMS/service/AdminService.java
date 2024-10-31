package com.Api.EMS.service;

import com.Api.EMS.dto.UserDTO;
import com.Api.EMS.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AdminService {
    Mono<User> createUser(UserDTO<String> userDTO);
    Mono<User> updateUser(String userId, UserDTO<String> userDTO);
    Mono<Void> deleteUser(String userId);
    Flux<User> getAllUsers();
    Mono<User> getUserById(String userId);
}
