package com.Api.EMS.config;

import com.Api.EMS.security.JwtAuthenticationFilter;
import com.Api.EMS.service.impl.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                          CustomUserDetailsService customUserDetailsService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager() {
        return authentication -> customUserDetailsService.findByUsername(authentication.getName())
                .flatMap(userDetails -> {
                    String password = authentication.getCredentials().toString();
                    if (passwordEncoder().matches(password, userDetails.getPassword())) {
                        return Mono.just(new UsernamePasswordAuthenticationToken(
                                userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities()));
                    } else {
                        return Mono.empty();
                    }
                });
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/api/auth/signup", "/api/auth/login").permitAll()
                        .pathMatchers("/api/admin/**").hasRole("ADMIN")
                        .pathMatchers("/api/director/**").hasAnyRole("ADMIN", "DIRECTOR")
                        .pathMatchers("/api/manager/**").hasAnyRole("ADMIN", "DIRECTOR", "MANAGER")
                        .pathMatchers("/api/employee/**").hasAnyRole("ADMIN", "DIRECTOR", "MANAGER", "EMPLOYEE")
                        .anyExchange().authenticated()
                )
                .addFilterAt(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHORIZATION)
                .authenticationManager(reactiveAuthenticationManager())
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(formLogin -> formLogin.disable())
                .build();
    }
}
