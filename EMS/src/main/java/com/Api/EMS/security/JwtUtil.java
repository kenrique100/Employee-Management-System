package com.Api.EMS.security;

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

    @Value("${jwt.refresh-expiration-ms}")
    private long refreshExpirationMs;

    private SecretKey key;

    @PostConstruct
    private void init() {
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String generateAccessToken(String username, List<String> roles) {
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpirationMs))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String getUsernameFromToken(@NonNull String token) {
        return getClaimsFromToken(token).getBody().getSubject();
    }

    public boolean validateToken(@NonNull String token) {
        try {
            getClaimsFromToken(token);
            return true;
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token", ex);
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token", ex);
        } catch (MalformedJwtException ex) {
            log.error("Malformed JWT token", ex);
        } catch (JwtException ex) {
            log.error("Invalid JWT token", ex);
        }
        return false;
    }


    public String refreshAccessToken(@NonNull String refreshToken) {
        if (validateToken(refreshToken)) {
            String username = getUsernameFromToken(refreshToken);
            List<String> roles = getRolesFromToken(refreshToken);
            return generateAccessToken(username, roles);
        } else {
            throw new JwtException("Invalid or expired refresh token.");
        }
    }


    private Jws<Claims> getClaimsFromToken(@NonNull String token) {
        JwtParser parser = Jwts.parserBuilder().setSigningKey(key).build();
        return parser.parseClaimsJws(token);
    }

    public List<String> getRolesFromToken(String token) {
        Claims claims = getClaimsFromToken(token).getBody();
        List<?> rawRoles = claims.get("roles", List.class);
        return rawRoles.stream()
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .collect(Collectors.toList());
    }
}
