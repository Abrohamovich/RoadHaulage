package ua.ithillel.roadhaulage.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ua.ithillel.roadhaulage.dto.AuthUserDto;

import java.util.Date;

@Component
public class JwtUtil {
    private static final Long TOKEN_VALID = 30 * 24 * 60 * 60 * 1000L; //30 days

    @Value("${jwt.secret}")
    private String TOKEN_SECRET;

    public String generateToken(AuthUserDto authUser) {
        return Jwts.builder()
                .id("" + authUser.getId())
                .subject(authUser.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + TOKEN_VALID))
                .signWith(Keys.hmacShaKeyFor(TOKEN_SECRET.getBytes()))
                .compact();
    }
    public Claims parseToken(String token) {
        JwtParser jwtParserBuilder = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(TOKEN_SECRET.getBytes()))
                .build();

        return jwtParserBuilder.parseSignedClaims(token).getPayload();
    }
}
