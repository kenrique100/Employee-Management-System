package com.Api.EMS.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .csrf().disable()
                .authorizeExchange()
                .pathMatchers("/admin/**").hasRole("ADMIN")
                .pathMatchers("/director/**").hasRole("DIRECTOR")
                .pathMatchers("/manager/**").hasRole("MANAGER")
                .pathMatchers("/employee/**").hasRole("EMPLOYEE")
                .anyExchange().authenticated()
                .and()
                .httpBasic()
                .and()
                .build();
    }

    // Add UserDetailsService Bean for in-memory or custom user details
}
