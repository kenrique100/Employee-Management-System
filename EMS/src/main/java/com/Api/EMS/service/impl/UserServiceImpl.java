package com.Api.EMS.service.impl;

import com.Api.EMS.dto.AuthRequest;
import com.Api.EMS.dto.AuthResponse;
import com.Api.EMS.dto.UserDTO;
import com.Api.EMS.model.Role;
import com.Api.EMS.model.User;
import com.Api.EMS.repository.UserRepository;
import com.Api.EMS.security.JwtUtil;
import com.Api.EMS.service.UserService;
import com.Api.EMS.utils.GUIDGenerator;
import com.Api.EMS.utils.UserUtil;
import com.Api.EMS.validation.UserValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService<User> {

    private final UserRepository userRepository;
    private final UserValidation userValidation;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserValidation userValidation,
                           PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.userValidation = userValidation;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<AuthResponse> signup(AuthRequest authRequest) {
        if (!authRequest.getRoles().contains("ADMIN")) {
            return Mono.error(new IllegalArgumentException("Only admins are allowed to sign up."));
        }
        return userRepository.findByUsername(authRequest.getUsername())
                .flatMap(existingUser -> Mono.error(new IllegalArgumentException("Username is already taken.")))
                .switchIfEmpty(Mono.defer(() -> {
                    User user = UserUtil.populateUserFields(new User(), authRequest, passwordEncoder);
                    user.setGuid(GUIDGenerator.generateGUID(16));
                    user.setRoles(List.of(Role.ADMIN));
                    return userRepository.save(user)
                            .map(savedUser -> {
                                String username = savedUser.getUsername();
                                List<String> roles = savedUser.getRoles().stream()
                                        .map(Role::name)
                                        .collect(Collectors.toList());
                                String accessToken = jwtUtil.generateAccessToken(username, roles);
                                String refreshToken = jwtUtil.generateRefreshToken(username);
                                return new AuthResponse("Success", accessToken, refreshToken);
                            });
                }))
                .cast(AuthResponse.class);
    }


    @Override
    public Mono<User> createUser(UserDTO<String> userDTO) {
        userValidation.validateUser(userDTO);
        User user = UserUtil.populateUserFields(new User(), userDTO, passwordEncoder);
        user.setGuid(GUIDGenerator.generateGUID(16)); // Generate GUID for new user
        return userRepository.save(user);
    }

    @Override
    public Flux<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Mono<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public Mono<User> updateUser(String id, UserDTO<String> userDTO) {
        return userRepository.findById(id).flatMap(user -> {
            userValidation.validateUser(userDTO);
            UserUtil.populateUserFields(user, userDTO, passwordEncoder); // Reuse populateUserFields method
            return userRepository.save(user);
        });
    }

    @Override
    public Mono<Boolean> deleteUser(String id) {
        return userRepository.findById(id)
                .flatMap(user -> userRepository.deleteById(id)
                        .then(Mono.just(true)))
                .defaultIfEmpty(false);
    }
}
