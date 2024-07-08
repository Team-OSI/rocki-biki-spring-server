package study.oauth2.auth.local.jwt;

import java.security.Key;
import java.util.Collections;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import study.oauth2.auth.local.service.CustomUserDetailsService;

@Slf4j
@RequiredArgsConstructor
@Component
public class TokenProvider {

    private static final long ACCESS_TOKEN_EXPIRE_TIME_IN_MILLISECONDS = 1000 * 60 * 30; // 30min
    @Value("${jwt.secret}")
    private String secret;
    private Key key;
    private final CustomUserDetailsService customUserDetailsService;

    @PostConstruct
    public void init() {
        byte[] key = Decoders.BASE64URL.decode(secret);
        this.key = Keys.hmacShaKeyFor(key);
    }

    public boolean validateToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("JWT_TOKEN".equals(cookie.getName())) {
                    String token = cookie.getValue();
                    return validateToken(token);
                }
            }
        }
        return false;
    }

    public boolean validateToken(String token) {

        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            return true;
        } catch (UnsupportedJwtException | MalformedJwtException exception) {
            log.error("JWT is not valid");
        } catch (SignatureException exception) {
            log.error("JWT signature validation fails");
        } catch (ExpiredJwtException exception) {
            log.error("JWT is expired");
        } catch (IllegalArgumentException exception) {
            log.error("JWT is null or empty or only whitespace");
        } catch (Exception exception) {
            log.error("JWT validation fails", exception);
        }

        return false;
    }

    public ResponseCookie createToken(Authentication authentication) {
        Date date = new Date();
        Date expiryDate = new Date(date.getTime() + ACCESS_TOKEN_EXPIRE_TIME_IN_MILLISECONDS);

        String token = Jwts.builder()
            .setSubject(authentication.getName())
            .setIssuedAt(date)
            .setExpiration(expiryDate)
            .signWith(key, SignatureAlgorithm.HS512)
            .compact();

        return ResponseCookie.from("JWT_TOKEN", token)
            .httpOnly(true)
            .secure(false) // HTTPS를 사용하는 경우에만 true로 설정
            .path("/")
            .maxAge(ACCESS_TOKEN_EXPIRE_TIME_IN_MILLISECONDS / 1000) // 초 단위로 변환
            .sameSite("Strict") // 또는 "Lax"
            .build();
    }

    public Authentication getAuthentication(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("JWT_TOKEN".equals(cookie.getName())) {
                    String token = cookie.getValue();
                    return getAuthentication(token);
                }
            }
        }
        return null;
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        UserDetails user = new User(claims.getSubject(), "", Collections.emptyList());

        return new UsernamePasswordAuthenticationToken(user, "", Collections.emptyList());
    }
}
