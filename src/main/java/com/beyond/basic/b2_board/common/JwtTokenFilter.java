package com.beyond.basic.b2_board.common;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtTokenFilter extends GenericFilter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // servletRequest 사용자의 요청 정보가 담겨있음

        HttpServletRequest request = (HttpServletRequest) servletRequest;       // 사용자 정보를 꺼내기 전 형변환
        String token = request.getHeader("Authorization");                   // token은 Header 안에 Authorization에 들어있음
        if (token == null) {
            // token이 없는 경우 다시 filterChain으로 되돌아가는 로직
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        
        // token이 있는 경우 토큰 검증 후 Authentication 객체 생성
    }
}
