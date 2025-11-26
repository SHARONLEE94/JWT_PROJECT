package com.lab.jwtmvc.security;

import com.lab.jwtcore.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse  response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Authorization 헤더에서 토큰 추출
        String bearer = request.getHeader("Authorization");
        String token = null;

        if(bearer != null && bearer.startsWith("Bearer ")) {
            token = bearer.substring(7);
        }

        // 2. 토큰이 없으면 그대로 통과
        if(token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. 토큰이 유효한지 검증
        String userId = JwtUtil.getUsername(token);
        if(userId != null && JwtUtil.validate(token)) {
            // 4. 유저 정보 가져오기
            UserDetails userDetails = userDetailsService.loadUserByUsername(userId);

            // 5. 인증 객체 생성
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                    );

            // 6. SecurityContext에 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 7. 다음 필터로 이동
        filterChain.doFilter(request, response);
    }

}
