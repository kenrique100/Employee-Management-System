package com.Api.EMS.service.impl;

import com.Api.EMS.repository.UserRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Primary
public class CustomUserDetailsService implements ReactiveUserDetailsService {

    private final UserRepository userRepository;
    private static final String DEFAULT_COMPANY_NAME = "DefaultCompany";

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return findByUsernameAndCompanyName(username, DEFAULT_COMPANY_NAME);
    }

    public Mono<UserDetails> findByUsernameAndCompanyName(String username, String companyName) {
        return userRepository.findByUsernameAndCompanyName(username, companyName)
                .map(user -> org.springframework.security.core.userdetails.User.builder()
                        .username(user.getUsername())
                        .password(user.getPassword())
                        .authorities(user.getRoles().stream()
                                .map(role -> "ROLE_" + role.name())  // Use role.name() for the enum to string conversion
                                .toArray(String[]::new))
                        .build());
    }
}
