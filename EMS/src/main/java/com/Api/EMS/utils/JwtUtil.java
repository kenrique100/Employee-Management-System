package com.Api.EMS.utils;

import com.Api.EMS.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.util.Date;

@Component
@Slf4j
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration-ms}")
    private long jwtExpirationMs;

    private SecretKey key;

    @PostConstruct
    private void init() {
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    // Generates a token using the provided Authentication object
    public String generateToken(Authentication authentication) {
        User userPrincipal = (User) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))  // Use current time
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    // Retrieves the username from the token
    public String getUsernameFromToken(String token) {
        return getClaimsFromToken(token).getBody().getSubject();
    }

    // Validates the token and returns true or false
    public boolean validateToken(String token) {
        try {
            getClaimsFromToken(token);
            return true;
        } catch (Exception ex) {
            log.error("Invalid JWT token", ex);
            return false;  // Return false directly in the catch block
        }
    }

    private Jws<Claims> getClaimsFromToken(String token) {
        JwtParser parser = Jwts.parserBuilder().setSigningKey(key).build();
        return parser.parseClaimsJws(token);
    }
}
