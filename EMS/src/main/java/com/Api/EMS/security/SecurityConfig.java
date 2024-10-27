package com.Api.EMS.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)  // Disable CSRF for WebFlux
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/admin/signup", "/login").permitAll()  // Open endpoints for registration and login
                        .anyExchange().authenticated()  // Require authentication for all other endpoints
                )
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)  // Enable HTTP Basic Authentication
                .formLogin(formLoginSpec -> formLoginSpec.loginPage("/login"));  // Set up form login

        return http.authenticationManager(authenticationManager()).build();  // Set authentication manager here
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private ReactiveAuthenticationManager authenticationManager() {
        return authentication -> Mono.just(new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), List.of()));
    }
}
