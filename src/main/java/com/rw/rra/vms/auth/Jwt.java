package com.rw.rra.vms.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

public class Jwt {
    private final Claims claims;
    private final SecretKey secretKey;

    public Jwt(Claims claims, SecretKey secretKey) {
        this.claims    = claims;
        this.secretKey = secretKey;
    }

    /** Has the token expired? */
    public boolean isExpired() {
        return claims.getExpiration().before(new Date());
    }

    /** The subject() claim holds the user’s UUID string */
    public UUID getUserId() {
        return UUID.fromString(claims.getSubject());
    }

    /** The “role” custom claim, e.g. “ROLE_ADMIN” or “ROLE_STANDARD” */
    public String getRole() {
        return claims.get("role", String.class);
    }

    /** Renders back to a compact JWT string for HTTP headers */
    @Override
    public String toString() {
        return Jwts.builder()
                .setClaims(claims)
                .signWith(secretKey)
                .compact();
    }
}
