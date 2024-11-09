package com.Api.EMS.service.impl;

import com.Api.EMS.dto.AuthRequest;
import com.Api.EMS.dto.AuthResponse;
import com.Api.EMS.dto.UserDTO;
import com.Api.EMS.model.Role;
import com.Api.EMS.model.User;
import com.Api.EMS.repository.UserRepository;
import com.Api.EMS.service.AdminService;
import com.Api.EMS.utils.GUIDGenerator;
import com.Api.EMS.utils.JwtUtil;
import com.Api.EMS.utils.UserUtil;
import com.Api.EMS.validation.UserValidation;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final UserValidation userValidation;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AdminServiceImpl(UserRepository userRepository, UserValidation userValidation, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
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

        return userRepository.existsByUsernameAndCompanyName(authRequest.getUsername(), authRequest.getCompanyName())
                .filter(exists -> !exists)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Username is already taken.")))
                .flatMap(exists -> {
                    User user = User.builder()
                            .guid(GUIDGenerator.generateGUID(8))
                            .companyName(authRequest.getCompanyName())
                            .username(authRequest.getUsername())
                            .password(passwordEncoder.encode(authRequest.getPassword()))
                            .roles(List.of(Role.ADMIN))
                            .build();

                    return userRepository.save(user)
                            .map(savedUser -> new AuthResponse(jwtUtil.generateToken(savedUser)));
                });
    }

    @Override
    public Mono<User> createUser(UserDTO<String> userDTO) {
        userValidation.validateUser(userDTO);
        User user = UserUtil.populateUserFields(new User(), userDTO);
        user.setGuid(GUIDGenerator.generateGUID(8));
        return userRepository.save(user);
    }

    @Override
    public Mono<User> updateUser(String id, UserDTO<String> userDTO) {
        userValidation.validateUser(userDTO);
        return userRepository.findById(id)
                .flatMap(user -> {
                    UserUtil.populateUserFields(user, userDTO);
                    return userRepository.save(user);
                });
    }

    @Override
    public Mono<Boolean> deleteUser(String id) {
        return userRepository.existsById(id)
                .flatMap(exists -> exists ? userRepository.deleteById(id).thenReturn(true) : Mono.just(false));
    }

    @Override
    public Flux<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Mono<User> findUserById(String id) {
        return userRepository.findById(id);
    }
}