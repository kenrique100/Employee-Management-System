package com.Api.EMS.service;

import com.Api.EMS.dto.UserDTO;
import com.Api.EMS.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<User> createUser(UserDTO userDTO);
    Flux<User> getAllUsers();
    Mono<User> getUserById(String id);
    Mono<User> updateUser(String id, UserDTO userDTO);
    Mono<Void> deleteUser(String id);
}
