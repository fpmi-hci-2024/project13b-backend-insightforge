package com.bookstore.dev.services.utils.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bookstore.dev.configs.exception.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.accessExpirationMs}")
    private long accessExpirationMs;
    @Value("${jwt.refreshExpirationMs}")
    private long refreshExpirationMs;

    public String createJwtAccessToken(String username) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date expiryDate = new Date(nowMillis + accessExpirationMs);
        return createJwtToken(username, now, expiryDate);
    }

    public String createJwtAccessToken() {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date expiryDate = new Date(nowMillis + accessExpirationMs);
        return createJwtToken(now, expiryDate);
    }

    public String createJwtRefreshToken() {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date expiryDate = new Date(nowMillis + refreshExpirationMs);
        return createJwtToken(now, expiryDate);
    }

    public String createJwtRefreshToken(String username) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date expiryDate = new Date(nowMillis + refreshExpirationMs);
        return createJwtToken(username, now, expiryDate);
    }

    private String createJwtToken(String username, Date now, Date expiryDate) {
        return JWT.create()
                .withSubject(username)
                .withIssuedAt(now)
                .withExpiresAt(expiryDate)
                .withJWTId(UUID.randomUUID().toString())
                .sign(Algorithm.HMAC256(secret));
    }

    private String createJwtToken(Date now, Date expiryDate) {
        return JWT.create()
                .withIssuedAt(now)
                .withExpiresAt(expiryDate)
                .withJWTId(UUID.randomUUID().toString())
                .sign(Algorithm.HMAC256(secret));
    }

    public String validateAndGetUsernameFromJwtToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getSubject();
        } catch (JWTVerificationException exception) {
            System.out.println(exception.toString());
            return null;
        }
    }

    public void validateToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                    .build();
            verifier.verify(token);
        } catch (JWTVerificationException exception) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Недопустимый токен.");
        }
    }

    public Date getExpiryDate(String token) {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(secret))
                .build()
                .verify(token);
        return decodedJWT.getExpiresAt();
    }
}
