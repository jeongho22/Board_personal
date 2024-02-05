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
                        .antMatchers("/signup", "/api/signup").permitAll()
                        .anyRequest().authenticated()
//                        .anyRequest().permitAll()
                )
                .formLogin(form -> form
                        .defaultSuccessUrl("/articles", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login")
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

    //@Bean
    //PasswordEncoder 빈을 정의해 두면 애플리케이션의 어느 곳에서든지 주입받아 사용할 수 있습니다.
    //1.    public *UserService*(PasswordEncoder passwordEncoder, UserRepository userRepository) {
    //        this.passwordEncoder = passwordEncoder;
    //        this.userRepository = userRepository;
    //    }
    //2.    @Autowired
    //    public *SecurityConfig*(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
    //        this.userDetailsService = userDetailsService;
    //        this.passwordEncoder = passwordEncoder;
    //    }

}


