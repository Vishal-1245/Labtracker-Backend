package com.college.labtracker.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

// Service to generate, parse, and validate JSON Web Tokens using modern JJWT 0.12.x API
@Service
public class JwtService {

    @Value("${jwt.secret:404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970}")
    private String secretKey;

    @Value("${jwt.expiration-ms:86400000}")
    private long jwtExpirationMs;

    // Helper to obtain cryptographic HMAC SHA secret key
    private SecretKey getSigningKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Extract the username (subject) stored in the token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract role custom claim stored in the token
    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    // Extract full name custom claim stored in the token
    public String extractFullName(String token) {
        return extractAllClaims(token).get("fullName", String.class);
    }

    // Generic claim extractor using functional interface
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Parse all claims using the modern JJWT 0.12 API: Jwts.parser().verifyWith(key).build().parseSignedClaims(token)
    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Generate token for an authenticated user with custom role and fullName claims
    public String generateToken(String username, String role, String fullName) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", role);
        extraClaims.put("fullName", fullName);
        return buildToken(extraClaims, username, jwtExpirationMs);
    }

    // Construct the JWT using modern JJWT 0.12 builder: Jwts.builder().subject(...).signWith(...)
    private String buildToken(Map<String, Object> extraClaims, String subject, long expiration) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .claims(extraClaims)
                .subject(subject)
                .issuedAt(new Date(now))
                .expiration(new Date(now + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    // Validate if the token belongs to the given user and is not expired
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // Check if token has passed its expiration timestamp
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Extract expiration date from token
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
