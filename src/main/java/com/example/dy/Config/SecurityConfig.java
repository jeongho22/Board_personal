
package com.example.dy.Config;
import com.example.dy.Service.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    public SecurityConfig(@Lazy CustomOAuth2UserService customOAuth2UserService) {
        this.customOAuth2UserService = customOAuth2UserService;

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {


        http
                // 1.기존 로그인 설정
                .cors().and() // 인가(접근권한) 설정
                .authorizeRequests(auth -> auth
                        .antMatchers("/articles","/signup","/api/signup").permitAll()
                        .antMatchers("/articles/4").hasRole("ADMIN")                   // 한가지 권한 설정
                        .antMatchers("/articles/5").hasAnyRole("ADMIN","USER")  // 여러 가지 권한 설정 가능
                        .anyRequest().authenticated()                                   // 나머지 경로는 로그인 한 사용자만 접근 가능
                )
                .formLogin(form -> form
                        .loginPage("/login") // 내가 커스텀마이징 한 로그인 페이지
                        .loginProcessingUrl("/loginPass")  // 로그인 폼 데이터 post 받음.
                        .defaultSuccessUrl("/articles", true)
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                        .permitAll()
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                )
                // 사이트 위변조 요청 방지
                // .csrf().disable()

                //2. 카카오 로그인 설정

                .oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint()
                .userService(customOAuth2UserService) // CustomOAuth2UserService를 사용하도록 설정
                .and()
                .loginPage("/login") // OAuth2 로그인을 위한 사용자 정의 로그인 페이지 경로 설정
                .defaultSuccessUrl("/articles") // OAuth2 로그인 성공 시 리디렉션할 URL
                .failureUrl("/loginFailure") // OAuth2 로그인 실패 시 리디렉션할 URL
        )
                // 세션 관리 설정 추가(1.중복 로그인, 2.세션 변경)
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
}

