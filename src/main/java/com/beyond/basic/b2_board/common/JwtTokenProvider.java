package com.beyond.basic.b2_board.common;

import com.beyond.basic.b2_board.author.domain.Author;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.expirationAt}")
    private int expirationAt;

    @Value("${jwt.secretKeyAt}")
    private String secretKeyAt;

    private Key secret_at_key;

    // Spring Bean 이 만들어지는 시점에 Bean이 만들어진 직후에 아래 메서드 바로 실행
    @PostConstruct
    public void init() {
        secret_at_key = new SecretKeySpec(java.util.Base64.getDecoder().decode(secretKeyAt)
                , SignatureAlgorithm.HS512.getJcaName());
    }

    public String createAtToken(Author author) {
        String email = author.getEmail();
        String role = author.getRole().toString();
        init();
        // claims 는 payload (사용자 정보)를 의미
        Claims claims = Jwts.claims().setSubject(email);        // payload 에 email 할당 (subject는 1개만 할당 가능)
        claims.put("role", role);                               // 주된 키 값을 제외한 나머지 사용자 정보는 put 사용하여 key:value 세팅

        Date now = new Date();
        String token = Jwts.builder()
                .setClaims(claims)                                              // payload 세팅
                .setIssuedAt(now)                                               // 발행 시간
//                .setExpiration(new Date(now.getTime() + 30 * 90 * 1000L))     // 만료 시간 (현재 시간 기준 30분(ms) 세팅)
                .setExpiration(new Date(now.getTime() + expirationAt * 90 * 1000L))
                .signWith(secret_at_key)                                        // secretkey를 통해 signature 생성
                .compact();
        return token;
    }
}
