package com.revature.Flumblr.services;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.revature.Flumblr.dtos.responses.Principal;
import com.revature.Flumblr.utils.custom_exceptions.InvalidTokenException;

import io.jsonwebtoken.Claims;

import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

/**
 * Service class for handling JWT token generation and validation.
 */
@Service
public class TokenServices {
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    public String generateToken(Principal userPrincipal) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userPrincipal.getId());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // Set expiration time to 10
                                                                                           // hours
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    // //  Validates the provided JWT token against the user principal.
    // public boolean validateToken(String token, Principal userPrincipal) {
    //     String tokenUsername = extractUsername(token);
    //     return tokenUsername.equals(userPrincipal.getUsername());
    // }


    //  Validates the provided JWT token against the user principal.
    public boolean validateToken(String token, String user_id) {
        String tokenUsername = extractUsername(token);
        return tokenUsername.equals(user_id);
    }


    //  /** Extracts the username from the JWT token.
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts a claim from the JWT token using the provided claims resolver
     * function.
     * @param claimsResolver The claims resolver function.
     * @param <T>            The type of the claim.
     */

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts all claims from the JWT token.
     *
     * @param token The JWT token.
     * @return The extracted claims.
     */
    private Claims extractAllClaims(String token) {
        try {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        } catch (SignatureException e) {
        throw new InvalidTokenException("Your token was invalid");
    }
    }
   public boolean isJwtExpired(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();

            Date expirationDate = claims.getExpiration();
            Date currentDate = new Date();

            return expirationDate != null && currentDate.after(expirationDate);
        } catch (Exception e) {
            // Token is invalid or parsing failed
            return true;
        }
    }
}
