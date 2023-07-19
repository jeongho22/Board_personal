package com.example.dy.Config;  // 이 코드는 com.example.dy.Config 패키지에 위치하고 있습니다.

import com.example.dy.serivce.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration  // 이 클래스는 스프링 설정 클래스임을 나타냅니다.
@EnableWebSecurity  // 웹 보안을 활성화합니다.
@EnableGlobalMethodSecurity(prePostEnabled = true) // 메소드 기반의 보안 설정 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {  // 웹 보안 설정 클래스를 정의합니다.
    private UserDetailsServiceImpl userDetailsService;  // 사용자 정보 조회를 위한 서비스를 저장할 필드입니다.
    private BCryptPasswordEncoder bCryptPasswordEncoder;  // 비밀번호 암호화를 위한 객체를 저장할 필드입니다.
    private CustomAuthenticationFailureHandler failureHandler;  // 인증 실패 시 처리를 위한 객체를 저장할 필드입니다.


    @Autowired  // 의존성 주입을 위한 어노테이션입니다.
    public SecurityConfig(UserDetailsServiceImpl userDetailsService,
                          BCryptPasswordEncoder bCryptPasswordEncoder,
                          CustomAuthenticationFailureHandler failureHandler) {
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.failureHandler = failureHandler;
    }

    public void configureGlobal(AuthenticationManagerBuilder auth)
            throws Exception {  // 인증 매니저를 구성하는 메소드입니다.
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);  // 사용자 정보 조회 서비스와 비밀번호 암호화 객체를 인증 매니저에 등록합니다.
    }

    @Override  // 상위 클래스의 메소드를 오버라이드합니다.
    protected void configure(HttpSecurity http) throws Exception {  // HTTP 보안 설정을 위한 메소드입니다.
        http
                .csrf()  // csrf 공격 방어를 위한 설정을 시작합니다.
                .ignoringAntMatchers("/boardwriting/**","/users/delete")  // "/api/**" 패턴의 URL에 대해서는 csrf 보안을 무시합니다.
                .and()  // 설정 이어가기 위한 연결 메소드입니다.
                .authorizeRequests()  // 요청에 대한 보안을 설정합니다.
                .antMatchers("/register","/check-username").permitAll()  // "/register" 패턴의 URL은 모든 사용자가 접근할 수 있습니다.
//                .antMatchers("/users").hasRole("ADMIN")  // "/users" URL은 ADMIN 권한을 가진 사용자만 접근할 수 있습니다.
                .anyRequest().authenticated()  // 그 외의 요청은 인증된 사용자만 접근할 수 있습니다.
                .and()  // 설정 이어가기 위한 연결 메소드입니다.
                .formLogin()  // 폼 기반 로그인에 대한 설정을 시작합니다.
                .loginPage("/login")  // 로그인 페이지의 URL을 설정합니다.
                .failureHandler(failureHandler)  // 로그인 실패 시 실행할 핸들러를 설정합니다.
                .defaultSuccessUrl("http://localhost:8090/list", true)  // 로그인 성공 시 리다이렉트할 URL을 설정합니다.
                .permitAll()  // 모든 사용자가 로그인 할 수 있습니다.
                .and()  // 설정 이어가기 위한 연결 메소드입니다.
                .logout()  // 로그아웃에 대한 설정을 시작합니다.
                .invalidateHttpSession(true)  // 로그아웃 시 세션을 무효화합니다.
                .clearAuthentication(true)  // 로그아웃 시 인증 정보를 지웁니다.
                .deleteCookies("JSESSIONID")  // 로그아웃 시 쿠키를 삭제합니다.
                .logoutSuccessUrl("/login")  // 로그아웃 성공 시 리다이렉트할 URL을 설정합니다.
                .permitAll();  // 모든 사용자가 로그아웃 할 수 있습니다.
        http
                .exceptionHandling()
                .accessDeniedPage("/access-denied"); // 사용자가 접근 권한이 없을 때 보여줄 페이지의 경로를 설정합니다.
    }



    }




//SecurityConfig 클래스는 Spring Security 설정을 정의하는 곳입니다.
// 이 클래스에서는 HTTP 보안을 구성하고, 사용자 세부 서비스를 등록하며, 비밀번호 인코더를 설정합니다.

// PasswordEncoderConfig 이후 과정임