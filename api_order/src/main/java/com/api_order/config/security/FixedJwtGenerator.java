package com.api_order.config.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class FixedJwtGenerator {

    @Value("${SECRET_KEY}")
    private String secretKey;

    private Key key;

    @PostConstruct
    public void init() {
        // Cria a Key a partir da secret
        key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String service) {
        return Jwts.builder()
                .claim("role", "SYSTEM") // Quem gera é o sistema
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600_000)) // 1 hora
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}