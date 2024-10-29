package com.Api.EMS.service;

import com.Api.EMS.dto.AuthRequest;
import com.Api.EMS.dto.AuthResponse;
import com.Api.EMS.dto.UserDTO;
import com.Api.EMS.model.User;

import java.util.List;
import java.util.Optional;

public interface AdminService {
    AuthResponse signup(AuthRequest authRequest);
    User createUser(UserDTO<String> userDTO);
    Optional<User> updateUser(Long id, UserDTO<String> userDTO);
    boolean deleteUser(Long id);
    List<User> getAllUsers();
    Optional<User> findUserById(Long id);
}
