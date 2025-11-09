package com.clinic.webapi.shared.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {

    private final Algorithm algorithm;
    private final JWTVerifier verifier;
    private final long expirationMs;

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration-ms}") long expirationMs) {
        this.algorithm = Algorithm.HMAC256(secret.getBytes());
        this.verifier = JWT.require(algorithm).build();
        this.expirationMs = expirationMs;
    }

    /** Genera un JWT con subject = userId y claims email + roles (array) */
    public String generateToken(UUID userId, String email, Set<String> roles) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + expirationMs);

        return JWT.create()
                .withSubject(userId.toString())
                .withClaim("email", email)
                .withArrayClaim("roles", roles.toArray(new String[0]))
                .withIssuedAt(now)
                .withExpiresAt(exp)
                .sign(algorithm);
    }

    public boolean validateToken(String token) {
        try {
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException ex) {
            return false;
        }
    }

    public UUID getUserId(String token) {
        DecodedJWT decoded = verifier.verify(token);
        String sub = decoded.getSubject();
        return UUID.fromString(sub);
    }

    public List<String> getRoles(String token) {
        DecodedJWT decoded = verifier.verify(token);
        List<String> roles = decoded.getClaim("roles").asList(String.class);
        return roles != null ? roles : Collections.emptyList();
    }

    public String getEmail(String token) {
        DecodedJWT decoded = verifier.verify(token);
        return decoded.getClaim("email").asString();
    }
}

