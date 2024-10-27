package com.Api.EMS.service.impl;

import com.Api.EMS.dto.AuthRequest;
import com.Api.EMS.dto.AuthResponse;
import com.Api.EMS.model.User;
import com.Api.EMS.repository.UserRepository;
import com.Api.EMS.security.JwtTokenProvider;
import com.Api.EMS.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Mono<AuthResponse> signup(AuthRequest authRequest) {
        if (authRequest.getRoles() == null || !authRequest.getRoles().contains("ADMIN")) {
            return Mono.error(new IllegalArgumentException("Only admins are allowed to sign up"));
        }

        return userRepository.existsByUsername(authRequest.getUsername())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new IllegalArgumentException("Username is taken"));
                    }

                    User user = User.builder()
                            .username(authRequest.getUsername())
                            .password(passwordEncoder.encode(authRequest.getPassword()))
                            .roles(List.of("ADMIN")) // Ensure role is set as "ADMIN"
                            .build();

                    return userRepository.save(user)
                            .map(savedUser -> new AuthResponse(jwtTokenProvider.generateToken(savedUser)));
                });
    }


    @Override
    public Mono<AuthResponse> login(AuthRequest authRequest) {
        return userRepository.findByUsername(authRequest.getUsername())
                .flatMap(user -> {
                    if (passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
                        return Mono.just(new AuthResponse(jwtTokenProvider.generateToken(user)));
                    }
                    return Mono.error(new RuntimeException("Invalid credentials"));
                })
                .switchIfEmpty(Mono.error(new RuntimeException("Invalid credentials")));
    }
}