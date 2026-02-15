package rpgshop.infraestructure.integration.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import rpgshop.infraestructure.config.JwtProperties;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class JwtService {
    private final SecretKey secretKey;
    private final JwtProperties jwtProperties;

    public JwtService(final JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.secretKey = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(final UUID userId, final String email, final String userType) {
        final Instant now = Instant.now();
        final Instant expiration = now.plus(jwtProperties.getExpirationHours(), ChronoUnit.HOURS);

        return Jwts.builder()
            .subject(userId.toString())
            .claim("email", email)
            .claim("userType", userType)
            .issuedAt(Date.from(now))
            .expiration(Date.from(expiration))
            .signWith(secretKey)
            .compact();
    }

    public Optional<Claims> validateToken(final String token) {
        try {
            final Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
            return Optional.of(claims);
        } catch (JwtException | IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public Optional<UUID> getUserIdFromToken(final String token) {
        return validateToken(token).map(claims -> UUID.fromString(claims.getSubject()));
    }

    public Optional<String> getEmailFromToken(final String token) {
        return validateToken(token).map(claims -> claims.get("email", String.class));
    }

    public Optional<String> getUserTypeFromToken(final String token) {
        return validateToken(token).map(claims -> claims.get("userType", String.class));
    }
}
