package ua.ithillel.roadhaulage.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import ua.ithillel.roadhaulage.dto.AuthUserDto;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class JwtUtilTests {
    private static final String secretKey = "121212121212121212121212121212121212121212121212";
    @InjectMocks
    private JwtUtil jwtUtil;
    private AuthUserDto authUser;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(jwtUtil, "TOKEN_SECRET", secretKey);

        authUser = new AuthUserDto();
        authUser.setId(1L);
        authUser.setEmail("test@example.com");
        authUser.setPassword("password");
    }

    @Test
    void generateToken() {
        String token = jwtUtil.generateToken(authUser);

        assertNotNull(token);
        assertFalse(token.isEmpty());

        Claims claims = jwtUtil.parseToken(token);
        assertEquals("1", claims.getId());
        assertEquals("test@example.com", claims.getSubject());
        assertNotNull(claims.getIssuedAt());
        assertNotNull(claims.getExpiration());
    }

    @Test
    public void parseToken_validToken() {
        String token = jwtUtil.generateToken(authUser);
        Claims claims = jwtUtil.parseToken(token);

        assertNotNull(claims);
        assertEquals("1", claims.getId());
        assertEquals("test@example.com", claims.getSubject());
    }

    @Test
    public void parseToken_invalidToken() {
        String invalidToken = "invalid.token.here";
        assertThrows(JwtException.class, () -> jwtUtil.parseToken(invalidToken));
    }

    @Test
    public void validateToken_validToken() {
        String token = jwtUtil.generateToken(authUser);
        boolean isValid = jwtUtil.validateToken(token, authUser);
        assertTrue(isValid);
    }

    @Test
    public void validateToken_expiredToken() {
        String expiredToken = Jwts.builder()
                .id("" + authUser.getId())
                .subject(authUser.getEmail())
                .issuedAt(new Date(System.currentTimeMillis() - 40 * 24 * 60 * 60 * 1000L))
                .expiration(new Date(System.currentTimeMillis() - 10 * 24 * 60 * 60 * 1000L))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();

        boolean isValid = jwtUtil.validateToken(expiredToken, authUser);
        assertFalse(isValid);
    }

    @Test
    public void validateToken_wrongUsername() {
        String token = jwtUtil.generateToken(authUser);
        AuthUserDto wrongUser = new AuthUserDto();
        wrongUser.setEmail("wrong@example.com");

        boolean isValid = jwtUtil.validateToken(token, wrongUser);
        assertFalse(isValid);
    }

    @Test
    public void validateToken_invalidToken() {
        String invalidToken = "invalid.token.here";
        boolean isValid = jwtUtil.validateToken(invalidToken, authUser);
        assertFalse(isValid);
    }
}