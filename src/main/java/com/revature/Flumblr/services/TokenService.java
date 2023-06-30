package com.revature.Flumblr.services;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.revature.Flumblr.utils.custom_exceptions.InvalidTokenException;
import com.revature.Flumblr.dtos.responses.Principal;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenService {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    public String generateJWT(Principal userPrincipal) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userPrincipal.getId());
        claims.put("role", userPrincipal.getRole());
        claims.put("email", userPrincipal.getEmail());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public void validateToken(String token, String id) {
        extractClaim(token, Claims::getExpiration);

        boolean idMatch = extractUserId(token).equals(id);

        if (!idMatch) {
            throw new InvalidTokenException("Invalid Token");
        }
    }

    public String extractUserId(String token) {

        return (String) extractAllClaims(token).get("id");
    }
}
