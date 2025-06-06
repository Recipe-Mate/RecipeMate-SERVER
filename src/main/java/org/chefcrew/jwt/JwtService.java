package org.chefcrew.jwt;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.chefcrew.common.constants.JWTConstants;
import org.chefcrew.common.exception.CustomException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

import static io.jsonwebtoken.Jwts.*;
import static org.chefcrew.common.exception.ErrorException.*;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @PostConstruct
    protected void init() {
        jwtSecret = Base64.getEncoder()
                .encodeToString(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    // JWT 토큰 발급
    public String issuedToken(String userId, Long tokenExpirationTime, String tokenType) {
        final Date now = new Date();

        // 클레임 생성
        final Claims claims = claims()
                .setSubject("token")
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenExpirationTime));

        //private claim 등록
        claims.put(JWTConstants.USER_ID, userId);
        if (tokenType == JWTConstants.ACCESS_TOKEN) {
            claims.put(JWTConstants.TOKEN_TYPE, JWTConstants.ACCESS_TOKEN);
        } else if (tokenType == JWTConstants.REFRESH_TOKEN) {
            claims.put(JWTConstants.TOKEN_TYPE, JWTConstants.REFRESH_TOKEN);
        }
        return builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims)
                .signWith(getSigningKey())
                .compact();
    }

    private SecretKey getSigningKey() {
        final byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // JWT 토큰 검증
    public JwtValidationType verifyToken(String token) {
        try {
            final Claims claims = getBody(token);
            if (claims.get(JWTConstants.TOKEN_TYPE).toString().equals(JWTConstants.ACCESS_TOKEN)) {
                return JwtValidationType.VALID_ACCESS;
            } else if (claims.get(JWTConstants.TOKEN_TYPE).toString().equals(JWTConstants.REFRESH_TOKEN)) {
                return JwtValidationType.VALID_REFRESH;
            } else {
                throw new CustomException(WRONG_TYPE_TOKEN_EXCEPTION);
            }
        } catch (MalformedJwtException e) {
            throw new CustomException(WRONG_TYPE_TOKEN_EXCEPTION);
        } catch (ExpiredJwtException e) {
            throw new CustomException(TIME_EXPIRED_TOKEN_EXCEPTION);
        } catch (IllegalArgumentException e) {
            throw new CustomException(UNKNOWN_TOKEN_EXCEPTION);
        } catch (UnsupportedJwtException e) {
            throw new CustomException(UNSUPPORTED_TOKEN_EXCEPTION);
        } catch (SignatureException e) {
            throw new CustomException(WRONG_SIGNATURE_TOKEN_EXCEPTION);
        }
    }

    private Claims getBody(final String token) {
        return parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // JWT 토큰 내용 확인
    public String getUserFromJwt(String token) {
        final Claims claims = getBody(token);
        Object userIdObj = claims.get(JWTConstants.USER_ID);
        if (userIdObj == null) {
            throw new CustomException(UNKNOWN_TOKEN_EXCEPTION);
        }
        return userIdObj.toString();
    }
}
