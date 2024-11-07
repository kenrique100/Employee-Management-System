package com.Api.EMS.service.impl;

import com.Api.EMS.dto.AuthRequest;
import com.Api.EMS.dto.AuthResponse;
import com.Api.EMS.model.User;
import com.Api.EMS.model.Role;  // Import Role enum
import com.Api.EMS.repository.UserRepository;
import com.Api.EMS.utils.JwtUtil;
import com.Api.EMS.utils.PasswordUtil;
import com.Api.EMS.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordUtil passwordUtil;
    private final JwtUtil jwtUtil;

    @Override
    public Mono<AuthResponse> signup(AuthRequest authRequest) {
        // Ensure only "ADMIN" can sign up
        if (authRequest.getRoles() == null || !authRequest.getRoles().contains("ADMIN")) {
            return Mono.error(new IllegalArgumentException("Only admins are allowed to sign up"));
        }

        return userRepository.existsByUsernameAndCompanyName(authRequest.getUsername(), authRequest.getCompanyName())
                .flatMap(exists -> exists ? Mono.error(new IllegalArgumentException("Username is taken")) : createNewUser(authRequest));
    }

    @Override
    public Mono<AuthResponse> login(AuthRequest authRequest) {
        return userRepository.findByUsernameAndCompanyName(authRequest.getUsername(), authRequest.getCompanyName())
                .flatMap(user -> isPasswordValid(authRequest.getPassword(), user)
                        ? Mono.just(new AuthResponse(jwtUtil.generateToken(user)))  // Use the updated method
                        : Mono.error(new RuntimeException("Invalid credentials")))
                .switchIfEmpty(Mono.error(new RuntimeException("Invalid credentials")));
    }

    private Mono<AuthResponse> createNewUser(AuthRequest authRequest) {
        // Use Role enum for roles
        User user = User.builder()
                .username(authRequest.getUsername())
                .password(passwordUtil.encodePassword(authRequest.getPassword()))
                .roles(List.of(Role.ADMIN))  // Use Role enum here
                .build();

        return userRepository.save(user)
                .map(savedUser -> new AuthResponse(jwtUtil.generateToken(savedUser)));  // Generate token using the updated method
    }

    private boolean isPasswordValid(String rawPassword, User user) {
        return passwordUtil.matches(rawPassword, user.getPassword());
    }
}
