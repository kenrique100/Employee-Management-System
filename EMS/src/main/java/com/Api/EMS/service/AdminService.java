package com.Api.EMS.service;

import com.Api.EMS.dto.UserDTO;
import com.Api.EMS.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AdminService {
    Mono<User> createUser(UserDTO<String> userDTO);
    Mono<User> updateUser(Long id, UserDTO<String> userDTO);
    Mono<Void> deleteUser(Long id);
    Flux<User> getAllUsers();
    Mono<User> findUserById(Long id);
}
