// src/main/java/com/suportflow/backend/security/JwtUtil.java
package com.suportflow.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    private final String secretKey;
    private final long jwtExpirationMs;

    public JwtUtil(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.expirationMs}") long jwtExpirationMs) {
        this.secretKey = secretKey;
        this.jwtExpirationMs = jwtExpirationMs;
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(UserDetails userDetails, Long entityId) {
        Map<String, Object> claims = new HashMap<>();
        // Check if it's a Cliente
        boolean isCliente = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_CLIENTE"));

        if (isCliente) {
            claims.put("Cliente", true); // Add the "Cliente: true" claim
        } else {
            // Add roles for regular users
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
            claims.put("roles", roles); // ADD ROLES
        }

        claims.put("entityId", entityId);
        return createToken(claims, userDetails);
    }


    private String createToken(Map<String, Object> claims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public List<String> extractRoles(String token) {
        final Claims claims = extractAllClaims(token);
        return (List<String>) claims.get("roles"); //Keep for possibel use.
    }
    public Boolean extractIsCliente(String token) {
        final Claims claims = extractAllClaims(token);
        Object clienteClaim = claims.get("Cliente");
        boolean isCliente = clienteClaim != null && (Boolean) clienteClaim;
        System.out.println("extractIsCliente: token=" + token + ", clienteClaim=" + clienteClaim + ", isCliente=" + isCliente); // Adicione este log
        return isCliente;
    }

    public Long extractEntityId(String token) {
        final Claims claims = extractAllClaims(token);
        return ((Number) claims.get("entityId")).longValue();
    }
}