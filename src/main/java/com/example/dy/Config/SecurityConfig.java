package com.example.dy.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors().and() // CORS 활성화
                .authorizeRequests(authz -> authz
                        // '/articles/new' 경로는 인증된 사용자만 접근 가능
                        .antMatchers("/articles/new").authenticated()

                        // 기타 '/articles/**' 경로는 모두 접근 가능
                        .antMatchers("/articles/**").permitAll()

                        // '/signup'과 '/api/signup' 경로는 모두 접근 가능
                        .antMatchers("/signup", "/api/signup").permitAll()

                        // 나머지 요청에 대해서는 인증된 사용자만 접근 가능
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .defaultSuccessUrl("/articles", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                        .permitAll()
                        .invalidateHttpSession(true) // 로그아웃 시 세션 종료
                        .clearAuthentication(true)   // 로그아웃 시 권한 제거
                )
                .csrf().disable(); // CSRF 보호 활성화

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



}


