package com.example.aivle.global.util.jwt;

import com.example.aivle.global.response.CustomException;
import com.example.aivle.global.response.ErrorCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.aivle.global.response.ErrorCode.INVALID_TOKEN;

@Slf4j
@Component
public class JwtTokenUtils {

    private final Key key;

    public JwtTokenUtils(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = secretKey.getBytes();
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public Authentication createAuthentication(Integer memberId) {
        return new UsernamePasswordAuthenticationToken(
                memberId,
                null,
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);
        if (claims.get("auth") == null) {
            throw new CustomException(INVALID_TOKEN, "권한 정보가 없는 토큰입니다.");
        }

        List<SimpleGrantedAuthority> authorities = Arrays.stream(claims.get("auth").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        User principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public String generateAccessToken(Authentication authentication) {

        String authority = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Date expiration = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 2); // 2일 유효

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim("auth", authority)
                .setExpiration(expiration)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT Token. " + e.getMessage());
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT Token. " + e.getMessage());
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT Token. " + e.getMessage());
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty. " + e.getMessage());
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        } catch (JwtException e) {
            log.error("JWT Token error: " + e.getMessage());
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        } catch (Exception e) {
            log.error("Exception error: " + e.getMessage());
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}