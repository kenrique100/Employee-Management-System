package com.Api.EMS.service.impl;

import com.Api.EMS.dto.AuthRequest;
import com.Api.EMS.dto.AuthResponse;
import com.Api.EMS.model.User;
import com.Api.EMS.repository.UserRepository;
import com.Api.EMS.service.AuthService;
import com.Api.EMS.utils.JwtUtil;
import com.Api.EMS.utils.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordUtil passwordUtil;
    private final JwtUtil jwtUtil;

    @Override
    public Mono<AuthResponse> login(AuthRequest authRequest) {
        return userRepository.findByUsernameAndCompanyName(authRequest.getUsername(), authRequest.getCompanyName())
                .flatMap(user -> isPasswordValid(authRequest.getPassword(), user)
                        ? Mono.just(new AuthResponse(jwtUtil.generateToken(user)))
                        : Mono.error(new RuntimeException("Invalid credentials")))
                .switchIfEmpty(Mono.error(new RuntimeException("Invalid credentials")));
    }


    private boolean isPasswordValid(String rawPassword, User user) {
        return passwordUtil.matches(rawPassword, user.getPassword());
    }
}
