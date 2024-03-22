//package com.example.dy.JWT;
//
//
//import io.jsonwebtoken.Jwts;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import javax.crypto.SecretKey;
//import javax.crypto.spec.SecretKeySpec;
//import java.nio.charset.StandardCharsets;
//import java.util.Date;
//
//@Slf4j
//@Component
//
//// JWT 에서 발급과 검증을 담당할 클래스 이다.
//public class JWTUtil {
//
//    private final SecretKey secretKey;
//
//    public JWTUtil(@Value("${spring.jwt.secret}")String secret) {
//        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm()); // 암호화 알고리즘
//    }
//
//    // 유저 이메일 확인 메소드
//    public String getEmail(String token) {
//        log.info("@2(이메일 시크릿키 변환) : {}",Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("email", String.class));
//
//        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("email", String.class);
//    }
//
//    // 유저 역할 확인 메소드
//    public String getRole(String token) {
//
//        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
//    }
//
//    // 만료일 확인 메소드
//    public Boolean isExpired(String token) {
//        log.info("@1(만기 토큰 시크릿키 변환) : {}",Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date()));
//
//        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
//    }
//
//
//    // 토큰 생성 메서드
//    public String createJwt(String email, String role, Long expiredMs) {
//        return Jwts.builder() // 빌더를 통해서 토큰만듬
//                .claim("email", email)                                         //payload 부분 (디코딩 쉬움)
//                .claim("role", role)                                           //payload 부분 (디코딩 쉬움)
//                .issuedAt(new Date(System.currentTimeMillis()))                  //payload 부분  (디코딩 쉬움)
//                .expiration(new Date(System.currentTimeMillis() + expiredMs))    //payload 부분  (디코딩 쉬움)
//                .signWith(secretKey)                                             // 암호화 진행
//                .compact();                                                     // 압축
//    }
//}
//
