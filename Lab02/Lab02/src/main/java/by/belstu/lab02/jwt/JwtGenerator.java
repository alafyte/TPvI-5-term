package by.belstu.lab02.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;


import java.util.Date;

@Component
@Log
public class JwtGenerator {

    @Value("${jwt.expiration}")
    private String JWT_EXPIRATION;

    @Value("${jwt.secret}")
    private String JWT_SECRET;

    public String generateToken(Authentication authentication, String userType) {
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expiryDate = new Date(currentDate.getTime() + Long.parseLong(JWT_EXPIRATION));

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(currentDate)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET)
                .claim("usertype", userType)
                .compact();
        return token;
    }

    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public String getUserTypeFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();
        return claims.get("usertype").toString();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            throw new AuthenticationCredentialsNotFoundException("JWT token is not valid " + token);
        }
    }
}