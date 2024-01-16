package com.madeeasy.security.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class ValidateToken {

    private static final String SECRET_KEY = "1adf0a4782f6e5674a79747fe58ea851b7581658d3715b12f4e0b12e999f307e";

    public String generateToken(String username) {
        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(username)
                .issuer("MadeEasy")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10)))
                .signWith(getSignKey())
                .compact();
    }

    public Claims getAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Date getExpirationDate(String token) {
        return getAllClaims(token).getExpiration();
    }

    public String getUserName(String token) {
        return getAllClaims(token).getSubject();
    }

    public boolean isTokenExpired(String token) {
        Date expirationDate = getExpirationDate(token);
        return expirationDate.before(new Date(System.currentTimeMillis()));
    }

    public boolean validateToken(String token) {
        String userName = getUserName(token);
        return !isTokenExpired(token);
    }
}