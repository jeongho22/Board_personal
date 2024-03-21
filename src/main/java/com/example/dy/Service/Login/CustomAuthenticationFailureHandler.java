package com.example.dy.Service.Login;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        setDefaultFailureUrl("/login?error=true"); // 실패 URL 설정
        super.onAuthenticationFailure(request, response, exception);

        // 추가적인 실패 로직을 여기에 구현할 수 있습니다.
        // 예를 들어, 로그 기록, 실패 카운터 증가 등
    }
}


