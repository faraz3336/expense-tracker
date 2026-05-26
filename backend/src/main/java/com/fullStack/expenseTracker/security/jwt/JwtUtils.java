package com.fullStack.expenseTracker.security.jwt;

import java.security.Key;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import com.fullStack.expenseTracker.security.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateJwtToken(Authentication authentication) {

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userPrincipal.getEmail()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key key() {
        byte[] keyBytes = resolveKeyBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private byte[] resolveKeyBytes() {
        byte[] rawKeyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);

        try {
            byte[] decodedKeyBytes = Decoders.BASE64.decode(jwtSecret);
            if (decodedKeyBytes.length >= 32) {
                logger.info("JWT secret resolved from Base64 value");
                return decodedKeyBytes;
            }
        } catch (RuntimeException ex) {
            logger.info("JWT secret is not Base64 encoded; using raw value");
        }

        if (rawKeyBytes.length >= 32) {
            logger.info("JWT secret resolved from raw value");
            return rawKeyBytes;
        }

        logger.warn("JWT secret is shorter than 32 bytes; deriving HS256 key with SHA-256");
        try {
            return MessageDigest.getInstance("SHA-256").digest(rawKeyBytes);
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 is not available for JWT key derivation", ex);
        }
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}
