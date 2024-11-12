package com.Api.EMS.service.impl;

import com.Api.EMS.dto.AuthRequest;
import com.Api.EMS.dto.AuthResponse;
import com.Api.EMS.model.Role;
import com.Api.EMS.repository.UserRepository;
import com.Api.EMS.security.JwtUtil;
import com.Api.EMS.service.AuthService;
import com.Api.EMS.utils.PasswordUtil;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordUtil passwordUtil;

    public AuthServiceImpl(UserRepository userRepository, JwtUtil jwtUtil, PasswordUtil passwordUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordUtil = passwordUtil;
    }

    @Override
    public Mono<AuthResponse> authenticate(AuthRequest authRequest) {
        return userRepository.findByUsername(authRequest.getUsername())
                .flatMap(user -> passwordUtil.matches(authRequest.getPassword(), user.getPassword())
                        ? Mono.just(new AuthResponse("Authentication successful",
                        jwtUtil.generateAccessToken(user.getUsername(),
                                user.getRoles().stream()
                                        .map(Role::name)
                                        .collect(Collectors.toList())),
                        jwtUtil.generateRefreshToken(user.getUsername())))
                        : Mono.error(new RuntimeException("Invalid credentials")));
    }
}
