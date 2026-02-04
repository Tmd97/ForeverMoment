package com.example.moment_forever.core.services.auth;

import com.example.moment_forever.data.entities.auth.AuthUser;
import com.example.moment_forever.data.entities.auth.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {

    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long jwtExpiration; // in milliseconds (e.g., 86400000 = 24 hours)

    @Value("${jwt.refresh-expiration}")
    private Long refreshExpiration; // in milliseconds (e.g., 604800000 = 7 days)

    // Generate signing key from secret
    private SecretKey getSigningKey() {
        // Convert string secret to cryptographic key
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * Generate JWT token for a user
     *
     * @param authUser The authenticated user
     * @return JWT token string
     */
    public String generateToken(AuthUser authUser) {
        logger.debug("Generating JWT token for user: {}", authUser.getUsername());

        // Create claims (payload data)
        Map<String, Object> claims = new HashMap<>();

        // Add user info to token payload
        claims.put("userId", authUser.getId());
        claims.put("username", authUser.getUsername());
        claims.put("externalUserId", authUser.getExternalUserId());

        // Add roles as comma-separated string
        // TODO: need to fix this, Not seems good coding design
        String roles = authUser.getUserRoles().stream()
                .map(authUserRole -> {
                    Role role = authUserRole.getRole();
                    return role != null ? role.getName() : "";
                })
                .filter(roleName -> !roleName.isEmpty())
                .collect(Collectors.joining(","));
        // Add user status
        claims.put("roles", roles);
        claims.put("enabled", authUser.isEnabled());
        claims.put("accountNonLocked", authUser.isAccountNonLocked());

        // Build and return the token
        return buildToken(claims, authUser.getUsername(), jwtExpiration);
    }

    /**
     * Generate refresh token (longer expiration, less data)
     */
    public String generateRefreshToken(AuthUser authUser) {
        logger.debug("Generating refresh token for user: {}", authUser.getUsername());

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", authUser.getId());
        claims.put("type", "refresh"); // Mark as refresh token

        return buildToken(claims, authUser.getUsername(), refreshExpiration);
    }

    /**
     * Build a JWT token with given claims
     */
    private String buildToken(Map<String, Object> claims, String subject, long expiration) {
        return Jwts.builder()
                .setClaims(claims)                     // Payload data
                .setSubject(subject)                   // User identifier
                .setIssuedAt(new Date(System.currentTimeMillis())) // When issued (iat)
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // When expires
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Sign with secret key
                .compact(); // Convert to string
    }

    /**
     * Extract username from token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extract user ID from token
     */
    public Long extractUserId(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("userId", Long.class);
    }

    /**
     * Extract external user ID (ApplicationUser.id) from token
     */
    public Long extractExternalUserId(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("externalUserId", Long.class);
    }

    /**
     * Extract roles from token
     */
    public String extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("roles", String.class);
    }

    /**
     * Check if token is expired
     */
    public boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    /**
     * Validate token (signature + expiration + user match)
     */
    public boolean validateToken(String token, UserDetails authUser) {
        final String username = extractUsername(token);
        return (username.equals(authUser.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Generic method to extract any claim
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extract all claims from token
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey()) // Verify signature with our secret
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}