//package com.example.dy.JWT;
//
//
//import com.example.dy.Dto.Login.CustomUserDetailsDto;
//import com.example.dy.Dto.OAuth2.CustomOAuth2UserDto;
//import com.example.dy.Service.Login.CustomUserDetailsService;
//import io.jsonwebtoken.ExpiredJwtException;
//import io.jsonwebtoken.JwtException;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.web.filter.OncePerRequestFilter;
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.Cookie;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//
//
//import java.io.IOException;
//import java.util.Arrays;
//
//
//@Slf4j
//public class JWTFilter extends OncePerRequestFilter { //한번만 동작하는 once 필터
//
//
//    private final CustomUserDetailsService customUserDetailsService;
//    private final JWTUtil jwtUtil;
//
//
//    public JWTFilter(CustomUserDetailsService customUserDetailsService,JWTUtil jwtUtil) {
//        this.customUserDetailsService = customUserDetailsService;
//        this.jwtUtil = jwtUtil;
//    }
//
//
//    // 1. 로그인 필터 에서 생성된 토큰 get 가져 오는 곳
//    private String getJwtFromCookies(HttpServletRequest request) {
//        Cookie[] cookies = request.getCookies();
//        log.info("6.cookies : {}", cookies);
//
//        if (cookies != null)
//        {
//            return Arrays.stream(cookies)
//                    .filter(cookie -> "jwtToken".equals(cookie.getName()))
//                    .findFirst()
//                    .map(Cookie::getValue)
//                    .orElse(null);
//        }
//
//        log.info("7.cookies null: {}", cookies);
//        return null;
//
//    }
//
//
//    // 2. Spring Security를 사용하여 HTTP 요청을 필터링하는 과정을 나타냅니다. 주요 목적은 JWT 토큰을 검증하여 사용자가 인증되었는지 확인한다.
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//
//        String jwtToken = getJwtFromCookies(request);
//        log.info("8.jwtToken : {}", jwtToken);
//
//        // 0. 토큰이 null 이 아니며 , 만기가 되지 않아야 한다.
//        if (jwtToken != null && ! jwtUtil.isExpired(jwtToken)) {
//
//            // 1. 토큰 에서 이메일 획득
//            String email = jwtUtil.getEmail(jwtToken);
//
//            log.info("9.JWT token validated for user: {}", email);
//
//            // 2. 타입 캐스팅 (인증 정보 로딩)
//            CustomUserDetailsDto customUserDetailsDto = (CustomUserDetailsDto) customUserDetailsService.loadUserByUsername(email);
//
//
//            // 3 .인증 토큰 생성 (Principal 인증된 사용자 , credentials 자격 증명, 사용자 권한)
//            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(customUserDetailsDto, null, customUserDetailsDto.getAuthorities());
//
//
//            // 4. 인증 토큰 설정
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        } else {
//
//            log.info("0.No JWT token found in request cookies");
//        }
//
//        // 5. 그 다음 필터에 넘겨 준다. => 그 다음 필터는
//        filterChain.doFilter(request, response);
//
////        http.addFilterBefore(new JWTFilter(customUserDetailsService,jwtUtil), LoginFilter.class);
////        // JWTFilter 를 LoginFilter 앞에 다가 넣어 준다.
////        http.addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration),jwtUtil), UsernamePasswordAuthenticationFilter.class);
////        // LoginFilter 를 UsernamePasswordAuthenticationFilter 에 넣어 준다.
//    }
//
//
//}