package com.example.dy.Config;

// 'Config' 패키지에 속한 클래스입니다. 주로 설정과 관련된 코드를 담당합니다.

import org.springframework.security.core.AuthenticationException;
// Spring Security에서 제공하는 예외 클래스입니다. 인증과 관련된 예외를 표현합니다.

import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
// Spring Security에서 제공하는 클래스입니다. 인증 실패시 동작을 정의합니다.

import org.springframework.stereotype.Component;
// Spring Framework에서 제공하는 애노테이션입니다. 이 클래스를 Component 빈으로 등록하고 관리하게 합니다.

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
// Java Servlet API에서 제공하는 클래스입니다. HTTP 요청과 응답을 다루고, 서블릿 예외를 표현합니다.

import java.io.IOException;
// Java에서 입출력 예외를 표현하는 클래스입니다.

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    // SimpleUrlAuthenticationFailureHandler 클래스를 상속받아 CustomAuthenticationFailureHandler 클래스를 정의하였습니다. 이 클래스는 Component 빈으로 등록됩니다.

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        // 인증 실패시 동작을 정의하는 메소드입니다.

        setDefaultFailureUrl("/login?error=true");
        // 인증 실패시 리다이렉트될 URL을 설정합니다. "/login?error=true"라는 URL로 리다이렉트 됩니다.

        super.onAuthenticationFailure(request, response, exception);
        // 상위 클래스인 SimpleUrlAuthenticationFailureHandler의 onAuthenticationFailure 메소드를 호출합니다. 이 메소드가 실제로 인증 실패시의 동작을 수행합니다.
    }
}


// Spring Security의 인증 실패 처리 로직을 재정의한 클래스입니다.
// 사용자 인증이 실패할 경우에 이 클래스의 onAuthenticationFailure 메소드가 호출되어 동작을 수행하게 됩니다.
// 현재 코드에서는 인증 실패시 "/login?error=true"라는 URL로 리다이렉트하도록 설정하였습니다.