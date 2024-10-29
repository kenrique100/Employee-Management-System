package com.Api.EMS.security;

import com.Api.EMS.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Qualifier;
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

    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(@Qualifier("customUserDetailsService") CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable) // Disable CSRF (consider security implications)
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/admin/signup", "/login").permitAll() // Permit these paths
                        .anyExchange().authenticated() // All other exchanges require authentication
                )
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable) // Disable basic auth
                .formLogin(formLoginSpec -> formLoginSpec.loginPage("/login")); // Custom login page

        return http.authenticationManager(authenticationManager()).build(); // Build the security filter chain
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Use BCrypt for password encoding
    }

    private ReactiveAuthenticationManager authenticationManager() {
        return authentication -> customUserDetailsService.findByUsername(authentication.getName())
                .map(userDetails -> new UsernamePasswordAuthenticationToken(
                        userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities()));
    }
}
