package com.tripPlanner.project.domain.login.auth.jwt;

import com.tripPlanner.project.domain.login.service.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;
    private final AuthService authService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 토큰 추출
        String token = resolveToken(request);

        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }


        if (jwtTokenProvider.validateToken(token)) {

            Authentication authentication = jwtTokenProvider.getTokenInfo(token);

            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {

        }
        filterChain.doFilter(request, response);
    }

    // 쿠키에서 토큰 정보 추출
    private String getTokenFromCookies(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("accessToken".equals(cookie.getName())) {
                    return cookie.getValue(); //쿠키 값 추출
                }
            }
        }
        return null;
    }

    //Request 헤더로부터 토큰 정보 추출
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);
            return token;
        }
        // 쿠키에서 토큰 추출
        String cookieToken = getTokenFromCookies(request);
        if (cookieToken != null) {
            return cookieToken;
        }

        return null;
    }

    private String resolveRefreshToken(HttpServletRequest request){
        String userid = request.getHeader("userid");

        String redisKey = "refreshToken:"+ userid;
        String refreshToken = redisTemplate.opsForValue().get(redisKey);
        if (refreshToken == null) {
            return null;
        }
        return refreshToken;
    }

}