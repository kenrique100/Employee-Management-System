package com.Api.EMS.utils;

import com.Api.EMS.model.User;
import com.Api.EMS.model.Role;  // Import Role enum
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration-ms}")
    private long jwtExpirationMs;

    private SecretKey key;

    // Initialize the SecretKey once when the class is loaded
    @PostConstruct
    private void init() {
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    // Generate a JWT token directly from a User object (e.g., for signup or login)
    public String generateToken(User user) {
        List<String> roles = user.getRoles().stream()  // Convert Role enum to List<String>
                .map(Role::name)
                .collect(Collectors.toList());

        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("roles", roles)  // Add roles claim as List<String>
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    // Get username from the JWT token
    public String getUsernameFromToken(@NonNull String token) {
        return getClaimsFromToken(token).getBody().getSubject();
    }

    // Validate the JWT token
    public boolean validateToken(@NonNull String token) {
        try {
            getClaimsFromToken(token);  // Try to get the claims; if fails, token is invalid
            return true;
        } catch (Exception ex) {
            log.error("Invalid JWT token", ex);
            return false;
        }
    }

    // Internal method to extract claims from the token
    private Jws<Claims> getClaimsFromToken(@NonNull String token) {
        JwtParser parser = Jwts.parserBuilder().setSigningKey(key).build();
        return parser.parseClaimsJws(token);
    }
}
