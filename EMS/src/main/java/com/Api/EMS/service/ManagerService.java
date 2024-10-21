package com.Api.EMS.service;

import com.Api.EMS.dto.UserDTO;
import com.Api.EMS.model.User;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ManagerService {
    Mono<User> createUser(UserDTO userDTO);
    Mono<User> updateUser(Long id, UserDTO userDTO);
    Mono<Void> deleteUser(Long id);
    List<User> getAllUsers();
    Mono<User> findUserById(Long id);
}
