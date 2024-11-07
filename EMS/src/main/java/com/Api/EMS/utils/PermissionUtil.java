package com.Api.EMS.utils;

import org.springframework.security.core.Authentication;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class PermissionUtil {

    private PermissionUtil() {
        // Private constructor to prevent instantiation
    }

    /**
     * Checks if the authenticated user has any of the specified roles.
     *
     * @param authentication The authentication object of the currently logged-in user.
     * @param roles          The roles to check for.
     * @return Mono<Boolean> that emits true if the user has any of the specified roles, otherwise false.
     */
    public static Mono<Boolean> hasPermission(Authentication authentication, String... roles) {
        Set<String> requiredRoles = Arrays.stream(roles)
                .map(role -> "ROLE_" + role)
                .collect(Collectors.toSet());

        return Mono.just(authentication.getAuthorities().stream()
                .anyMatch(authority -> requiredRoles.contains(authority.getAuthority())));
    }
}
