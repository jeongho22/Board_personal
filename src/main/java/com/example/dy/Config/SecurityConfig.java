
package com.example.dy.Config;
import com.example.dy.JWT.JWTUtil;
import com.example.dy.Service.Login.CustomAuthenticationFailureHandler;
import com.example.dy.Service.Login.CustomUserDetailsService;
import com.example.dy.Service.OAuth2.CustomOAuth2UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;


@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;


    public SecurityConfig(@Lazy CustomOAuth2UserService customOAuth2UserService,
                          AuthenticationConfiguration authenticationConfiguration,
                          JWTUtil jwtUtil,
                          CustomUserDetailsService customUserDetailsService) {
        this.customOAuth2UserService = customOAuth2UserService;
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtil = jwtUtil;
        this.customUserDetailsService = customUserDetailsService;

    }

    //AuthenticationManager Bean 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CustomAuthenticationFailureHandler customAuthenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }


//     1. 세션 저장
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {


        http

                // 1.기존 로그인 설정
                .cors().and() // 인가(접근권한) 설정
                .authorizeRequests(auth -> auth
                        .antMatchers("/signup","/login","/api/users/signup","/api/users/check-email","/articles","/").permitAll()
                        .antMatchers("/user/information","/api/users").hasRole("ADMIN")                   // 한가지 권한 설정
                        .antMatchers("/user/my_page").hasAnyRole("ADMIN","USER")  // 여러 가지 권한 설정 가능
                        .anyRequest().authenticated()                                   // 나머지 경로는 로그인 한 사용자만 접근 가능
                )

                .formLogin(form -> form
                        .loginPage("/login") // 내가 커스텀마이징 한 로그인 페이지
                        .loginProcessingUrl("/login")  // 로그인 폼 데이터 post 받음. 명시적으로 표시
                        .failureHandler(customAuthenticationFailureHandler()) // 로그인 실패 커스텀 핸들러 추가
                        .defaultSuccessUrl("/articles", true)
                        .permitAll()
                )


                .logout(logout -> logout
                        .logoutSuccessUrl("/articles")
                        .permitAll()
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .clearAuthentication(true)
                )

                // 여기에 CSRF 비활성화 추가
                .csrf().disable()


                //2. OAuth2 로그인 설정


                .oauth2Login(oauth2 -> oauth2                                   // 우리가 하고 싶은 Fillter 방식이 자동으로 다 들어있음
                .userInfoEndpoint()
                .userService(customOAuth2UserService)                           // CustomOAuth2UserService를 사용하도록 설정
                .and()
                .loginPage("/login")                                            // OAuth2 로그인을 위한 사용자 정의 로그인 페이지 경로 설정
                .defaultSuccessUrl("/articles",true)   // OAuth2 로그인 성공 시 리디렉션할 URL
                .failureUrl("/login")                        // OAuth2 로그인 실패 시 리디렉션할 URL
        )

                //3. 세션 관리 설정 추가(1.중복 로그인, 2.세션 변경)
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionConcurrency(concurrencyControl -> concurrencyControl
                                .maximumSessions(1)              // 동시 세션 최대 개수 설정
                                .maxSessionsPreventsLogin(false) // 동시 로그인 차단 설정 (true: 추가 로그인 차단, false: 기존 세션 만료)
                        )
                        .sessionFixation(sessionFixationConfigurer -> sessionFixationConfigurer.changeSessionId())
                                                                // 세션 고정 공격 방지를 위한 세션 ID 변경 설정
                );


        return http.build();
    }


//    //2. JWT 저장
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//
//        http.formLogin().disable();
//        http.csrf().disable();
//
//
//
//        // 인증 경로 설정
//        http.authorizeRequests(auth -> auth
//                .antMatchers("/login", "/signup", "/api/signup").permitAll()
//                .antMatchers("/").authenticated() // JWT 인증이 필요한 경로
//                .anyRequest().authenticated()
//        );
//
//        // 필터 등록 방법
//        // (로그인 필터 커스텀, 그위치 어디로 할 것인가? )
//
//        http.
//                addFilterBefore(new JWTFilter(customUserDetailsService,jwtUtil), LoginFilter.class).
//                addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration),jwtUtil), UsernamePasswordAuthenticationFilter.class);
//
//        // JWTFilter 를 LoginFilter 앞에 다가 넣어 준다.
//        // LoginFilter 를 UsernamePasswordAuthenticationFilter 에 넣어 준다.
//
//
//
//        // OAuth2 로그인 설정
//        http.oauth2Login(oauth2 -> oauth2
//                .userInfoEndpoint()
//                .userService(customOAuth2UserService)
//                .and()
//                .loginPage("/login")
//                .defaultSuccessUrl("/articles")
//                .failureUrl("/loginFailure")
//
//
//        );
//
//        http.logout(logout -> logout
//                .logoutUrl("/logout") // 로그아웃 처리 URL
//                .logoutSuccessUrl("/login") // 로그아웃 성공 후 리다이렉션할 URL
//                .invalidateHttpSession(true) // 세션 무효화
//                .deleteCookies("JSESSIONID", "jwtToken") // 쿠키 삭제
//        );
//
//
//        // 세션 관리 정책 조정
//        http.sessionManagement(session -> session
//                .sessionCreationPolicy(SessionCreationPolicy.NEVER)
//        );
//
//        return http.build();
//    }


}




