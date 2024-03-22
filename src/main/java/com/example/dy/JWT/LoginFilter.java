//package com.example.dy.JWT;
//
//
//import com.example.dy.Dto.Login.CustomUserDetailsDto;
//import com.example.dy.Dto.Login.UserRequestDto;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.AuthenticationServiceException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import javax.servlet.FilterChain;
//
//import javax.servlet.http.Cookie;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//
//
//
//
////1. formlogin 을 disable 해놨기 때문에 커스텀 해줘야한다 직접.
////2. 클라이언트 측 저장, formlogin 에서는 기본적(UsernamePasswordAuthenticationFilter) 으로 활성화 되어있음. 로그인을 할때 가로챔
//@Slf4j
//public class LoginFilter extends UsernamePasswordAuthenticationFilter {
//
//    private final AuthenticationManager authenticationManager;
//    private final JWTUtil jwtUtil;
//    public LoginFilter(AuthenticationManager authenticationManager,JWTUtil jwtUtil) {
//
//        this.authenticationManager = authenticationManager;
//        this.jwtUtil = jwtUtil;
//
//    }
//
//
//    //3. UsernamePasswordAuthenticationFilter은 무조건 override attemptAuthentication 해줘야한다
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//        try {
//
//            UserRequestDto loginRequest = new ObjectMapper().readValue(request.getInputStream(), UserRequestDto.class);
//            log.info("1.Login attempt received for email: {}", loginRequest.getUsername());
//
//            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
//            log.info("2.UsernamePasswordAuthenticationToken: {}", authToken);
//
//
//            //4.authenticationManager 자동으로 검증하는곳 (*검증 방법은 DB->UserDetails->authenticationManager 에서 회원 정보를 가져옴)
//            return authenticationManager.authenticate(authToken);
//
//        } catch (IOException e) {
//            log.error("로그인 요청 파싱 error: {}", e.getMessage());
//            throw new AuthenticationServiceException("로그인 요청 파싱 error", e);
//        }
//    }
//
//
//
//    //5. 위에서 검증이 성공 하면 successfulAuthentication 실행이 된다.
//    @Override
//    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {
//
//        // 5-0.유저 객체를 알아 내기 위해 -> CustomUserDetailsDto (타입 캐스트)
//        CustomUserDetailsDto customUserDetails = (CustomUserDetailsDto) authentication.getPrincipal();
//
//        String email = customUserDetails.getUsername();
//        String role = customUserDetails.getAuthorities().toString();
//
//        log.info("3-1.Login successful for user: {}", email);
//        log.info("3-2.Login successful for user: {}", role);
//
//        //5-1. 토큰 생성
//        String token = jwtUtil.createJwt(email, role, 180000L); // 토큰 생성 100초
//
//
//        //5-2. 생성된  JWT 토큰을 HttpOnly 쿠키에 추가
//        Cookie jwtCookie = new Cookie("jwtToken", token); // 쿠키 생성
//        jwtCookie.setHttpOnly(true); // HttpOnly 설정
//        jwtCookie.setSecure(true); // Secure 플래그 설정
//        jwtCookie.setPath("/"); // 쿠키 경로 설정
//        jwtCookie.setMaxAge((180000 / 1000)); // 쿠키 유효 시간 설정 (초 단위) // 토큰 생성
//
//
//        response.addCookie(jwtCookie); // 응답에 쿠키 추가
//        response.setStatus(HttpServletResponse.SC_OK); // 200 응답
//
//        log.info("4.JWT token added to HttpOnly cookie for user: {}", email);
//        log.info("5.jwtCookie : {}", jwtCookie);
//    }
//
//
//    //6. 위에서 검증이 실패 하면 unsuccessfulAuthentication 실행이 된다.
//    @Override
//    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed){
//        log.info("1.Login failed: {}", failed.getMessage());
//        log.info("2.LoginFilter: Login attempt failed");
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 응답
//    }
//}