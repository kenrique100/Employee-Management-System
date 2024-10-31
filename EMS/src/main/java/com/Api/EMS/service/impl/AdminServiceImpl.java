package com.Api.EMS.service.impl;

import com.Api.EMS.dto.UserDTO;
import com.Api.EMS.model.User;
import com.Api.EMS.repository.UserRepository;
import com.Api.EMS.service.AdminService;
import com.Api.EMS.utils.UserUtil;
import com.Api.EMS.validation.UserValidation;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserValidation userValidation;

    public AdminServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, UserValidation userValidation) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userValidation = userValidation;
    }

    @Override
    public Mono<User> createUser(UserDTO<String> userDTO) {
        userValidation.validateUser(userDTO);
        return userRepository.existsByUsernameAndCompanyName(userDTO.getUsername(), userDTO.getCompanyName())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new IllegalArgumentException("User with this username and company name already exists"));
                    }
                    User user = new User();
                    UserUtil.populateUserFields(user, userDTO);
                    user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
                    return userRepository.save(user);
                });
    }

    @Override
    public Mono<User> updateUser(String userId, UserDTO<String> userDTO) {
        return userRepository.findById(userId)
                .flatMap(user -> {
                    UserUtil.populateUserFields(user, userDTO);
                    if (userDTO.getPassword() != null) {
                        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
                    }
                    return userRepository.save(user);
                });
    }

    @Override
    public Mono<Void> deleteUser(String userId) {
        return userRepository.deleteById(userId);
    }

    @Override
    public Flux<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Mono<User> getUserById(String userId) {
        return userRepository.findById(userId);
    }
}
