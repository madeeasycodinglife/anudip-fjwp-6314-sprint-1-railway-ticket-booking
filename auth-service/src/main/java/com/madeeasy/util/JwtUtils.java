package com.madeeasy.util;

import com.madeeasy.exception.UserNotFoundException;
import com.madeeasy.vo.UserResponseDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.crypto.SecretKey;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class JwtUtils {
    private static final String SECRET_KEY = "1adf0a4782f6e5674a79747fe58ea851b7581658d3715b12f4e0b12e999f307e";
    private final RestTemplate restTemplate;
    private final PasswordEncoder passwordEncoder;
    Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    public String generateToken(String email, String password) {

        // make a rest-call to user-service to get roles of the user
        String url = "http://user-service/user-service/get-user-by-email/" + email;
        UserResponseDTO userResponseDTO =
                null;
        try {
            userResponseDTO = this.restTemplate.getForEntity(url, UserResponseDTO.class).getBody();
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new UserNotFoundException("User not found");
            } else {
                // Handle other exceptions or rethrow them if needed
                throw e;
            }
        }

        assert userResponseDTO != null;
        if (userResponseDTO.getPassword() != null) {
            if (passwordEncoder.matches(password, userResponseDTO.getPassword())) {
                return generateAccessToken(email, userResponseDTO.getRoles());
            }
        }
        return null;
    }

    public String generateAccessToken(String email, List<String> roles) {
        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(email)
                .claim("roles", roles)
                .issuer("madeeasycodinglife")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1)))
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

    public boolean validateToken(String token, String userName) {
        return userName.equals(getUserName(token)) && !isTokenExpired(token);
    }


    public List<String> getRolesFromToken(String token) {
        Claims claims = getAllClaims(token);

        Object rolesObject = claims.get("roles");

        List<String> roles = new ArrayList<>();

        if (rolesObject instanceof List<?>) {
            for (Object role : (List<?>) rolesObject) {
                if (role instanceof String) {
                    roles.add((String) role);
                }
                // Add additional checks or handling if needed
            }
        }
        return roles;
    }


    /**
     * custom method to generate a 256 random bit .
     */
    public static String generate256BitKey() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] keyBytes = new byte[32]; // 256 bits is 32 bytes
        secureRandom.nextBytes(keyBytes);
        // Convert the byte array to a hexadecimal string
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : keyBytes) {
            stringBuilder.append(String.format("%02x", b));
        }
        return stringBuilder.toString();
    }
}
