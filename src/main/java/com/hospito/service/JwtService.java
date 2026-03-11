package com.hospito.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {
    private static final String SECRET_KEY = "iamsujitkumarvipulfrommotiharibiharcurrentlylivinginbanglore";
    private static final long EXPIRATION_TIME = 86400000;

    public String generateToken(String username,String role){
        return Jwts.builder()
                .subject(username)
                .claim("role",role)
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusMillis(EXPIRATION_TIME)))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    public String extractRole(String token) {
        return extractClaims(token).get("role").toString();
    }
    public String validateTokenAndGetUsername(String token){
        try {
            Claims claims = extractClaims(token);
            return claims.getSubject();
        }catch (Exception e){
            throw new JwtException("Invalid token");
        }
    }
}
