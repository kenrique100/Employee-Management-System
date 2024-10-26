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
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Mono<AuthResponse> signup(AuthRequest authRequest) {
        return userRepository.existsByUsername(authRequest.getUsername())
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new IllegalArgumentException("Username is taken"));
                    }
                    // Continue with user registration
                    List<String> roles = authRequest.getRoles() != null ? authRequest.getRoles() : List.of("ADMIN");
                    User user = User.builder()
                            .username(authRequest.getUsername())
                            .password(passwordEncoder.encode(authRequest.getPassword()))
                            .roles(roles)
                            .build();

                    return userRepository.save(user)
                            .subscribeOn(Schedulers.boundedElastic())
                            .map(savedUser -> new AuthResponse(jwtTokenProvider.generateToken(savedUser)));
                });
    }


    @Override
    public Mono<AuthResponse> login(AuthRequest authRequest) {
        return userRepository.findByUsername(authRequest.getUsername())
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(user -> {
                    // Check if the password matches
                    if (passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
                        return Mono.just(new AuthResponse(jwtTokenProvider.generateToken(user))); // Use user directly
                    } else {
                        return Mono.error(new RuntimeException("Invalid credentials"));
                    }
                })
                .switchIfEmpty(Mono.error(new RuntimeException("Invalid credentials")));
    }
}
