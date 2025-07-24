package com.beyond.basic.b2_board.common;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity           // @PreAuthorize 사용하기 위한 설정
public class SecurityConfig {   // 로그인 관련 설정 정보

    private final JwtTokenFilter jwtTokenFilter;
    private final JwtAuthenticationHandler jwtAuthenticationHandler;
    private final JwtAuthorizationHandler jwtAuthorizationHandler;


    // 내가 만든 객체는 @Component, 외부 라이브러리를 활용한 객체는 @Bean + @Configuration
    // @Bean은 메서드 위에 붙여 return 되는 객체를 싱글톤 객체로 생성
    // @Component는 클래스 위에 붙여 클래스 자체를 싱글톤 객체로 생성
    // filer 계층에서 filter 로직을 커스텀
    @Bean       // return 되는 객체를 싱글톤 객체로 생성
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        
        // authentication 객체가 생성되지 않으면 Controller까지 가지도 못함
        return httpSecurity
                // cors() : 특정 도메인에 대한 허용 정책, postman 은 cors 정책에 적용X (웹 브라우저가 아니기 때문에 도메인 없음)
                .cors(c -> c.configurationSource(corsConfiguration()))
                // csrf(보안 공격 중 하나로서 타 사이트의 쿠키 값을 꺼내서 탈취하는 공격) 비활성화(대비하지 않을 것)
                // 세션 기반 로그인(MVC 패턴, SSR)에서는 CSRF 별도 설정하는 것이 일반적
                // 토큰 기반 로그인(REST API 서버, CSR)에서는 CSRF 설정하지 않는 것이 일반적
                .csrf(AbstractHttpConfigurer::disable)
                // httpBasic : email/pw를 인코딩하여 인증(전송)하는 방식. 간단한 인증의 경우에만 사용
                .httpBasic(AbstractHttpConfigurer::disable)
                // 세션 기반 로그인 방식 비활성화
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 검증 단계
                // token을 검정하고, token 검증을 통해 Authentication 객체 생성
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(e ->
                        e.authenticationEntryPoint(jwtAuthenticationHandler)           // 401의 경우
                                .accessDeniedHandler(jwtAuthorizationHandler)          // 403의 경우
                )
                // 예외 API 정책 설정
                // authenticated() : 예외를 제외한 모든 요청에 대해서 Authentication 객체가 생성되기를 요구
                .authorizeHttpRequests(a -> a.requestMatchers("/author/create", "/author/doLogin")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .build();
    }


    private CorsConfigurationSource corsConfiguration(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("*"));                            // 모든 HTTP(get, post 등) 메서드 허용 (허용 도메인 명시)
        configuration.setAllowedHeaders(Arrays.asList("*"));                            // 모든 헤더 요소(Authorization 등) 허용
        configuration.setAllowCredentials(true);                                        // 자격 증명 허용
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);                 //모든 url 패턴에 대해 cors 설정 적용
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
