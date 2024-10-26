package com.Api.EMS.security;

import com.Api.EMS.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Getter // Use @Getter if you need to access SECRET_KEY from outside, otherwise you can skip it
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String SECRET_KEY;  // This will be injected from application.properties

    private final long EXPIRATION_TIME = 86400000; // 1 day in milliseconds

    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("roles", user.getRoles())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    public boolean isTokenValid(String token) {
        return !extractClaims(token).getExpiration().before(new Date());
    }

    public String getUsernameFromToken(String token) {
        return extractClaims(token).getSubject();
    }
}
