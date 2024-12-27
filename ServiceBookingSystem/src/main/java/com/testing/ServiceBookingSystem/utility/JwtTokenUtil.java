package com.testing.ServiceBookingSystem.utility;
import com.testing.ServiceBookingSystem.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtTokenUtil {

    private Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512); // Properly generate a key

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 day validity
                .signWith(key)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            return false;
        }
    }


    // Extract username from the token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    // General method to extract a claim
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }


    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();
    }

    // Retrieve expiration date from the token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public static Collection<GrantedAuthority> getAuthorities(User user) {
        // Ensure role is non-null and map it to a SimpleGrantedAuthority
        if (user.getRole() == null) {
            return Collections.emptyList();
        }

        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().toString()));
    }
}