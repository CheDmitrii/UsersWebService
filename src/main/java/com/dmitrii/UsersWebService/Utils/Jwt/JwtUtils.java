package com.dmitrii.UsersWebService.Utils.Jwt;

import com.dmitrii.UsersWebService.Utils.Errors.JwtError;
import com.dmitrii.UsersWebService.Utils.Exeptions.JwtException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    @Value("${JWT.SECRET}")
    private String secret;

    public String generateJwtToken(Authentication authentication) {

        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public String getEmailFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            throw new JwtException(new JwtError("Invalid JWT token: {}" + e.getMessage(), null, null));
//            throw new MalformedJwtException("Invalid JWT token: {}" + e.getMessage());
        } catch (ExpiredJwtException e) {
            throw new JwtException(new JwtError("JWT token is expired: {}" + e.getMessage(), null, null));
//            throw new ExpiredJwtException(e.getHeader(), e.getClaims(), "JWT token is expired: {}" + e.getMessage());
        } catch (UnsupportedJwtException e) {
            throw new JwtException(new JwtError("JWT token is unsupported: {}" + e.getMessage(), null, null));
//            throw new UnsupportedJwtException("JWT token is unsupported: {}" + e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new JwtException(new JwtError("JWT claims string is empty: {}" + e.getMessage(), null, null));
//            throw new IllegalArgumentException("JWT claims string is empty: {}" + e.getMessage());
        }

    }
}
