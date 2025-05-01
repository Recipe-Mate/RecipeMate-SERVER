package org.chefcrew.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chefcrew.common.constants.JWTConstants;
import org.chefcrew.common.constants.WhiteListConstants;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws IOException, ServletException, IOException {
        for (String whiteUrl : WhiteListConstants.FILTER_WHITE_LIST) {
            if (pathMatcher.match(whiteUrl, request.getRequestURI())) {
                filterChain.doFilter(request, response);
                return;
            }
        }
        final String token = getJwtFromRequest(request);

        if (token != null) {
            // 토큰 검증
            JwtValidationType tokenType = jwtService.verifyToken(token);
            // JWT에서 userId를 추출하여 인증 객체 생성
            String userId = jwtService.getUserFromJwt(token);
            // 인증 객체를 SecurityContext에 설정
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userId, null, List.of() // 권한이 없다면 빈 리스트
            );

            if (tokenType == JwtValidationType.VALID_ACCESS) {
                request.setAttribute(JWTConstants.USER_ID, userId);
                request.setAttribute(JWTConstants.TOKEN_TYPE, tokenType);
            }

            SecurityContextHolder.getContext().setAuthentication(authentication);

        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            log.error(bearerToken.substring("Bearer ".length()));
            return bearerToken.substring("Bearer ".length());
        }
        return null;
    }

}