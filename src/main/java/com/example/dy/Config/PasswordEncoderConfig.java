package com.example.dy.Config;

// 'Config' 패키지에 속한 클래스입니다. 주로 설정과 관련된 코드를 담당합니다.

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Spring Framework에서 제공하는 애노테이션입니다. 설정 정보를 담은 클래스와 빈 객체를 생성하는 메소드를 표시하는 데 사용됩니다.

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// Spring Security에서 제공하는 클래스입니다. 비밀번호를 안전하게 암호화하는 데 사용됩니다.

@Configuration
public class PasswordEncoderConfig {
    // @Configuration 애노테이션을 통해 이 클래스는 Spring의 설정 클래스임을 명시하였습니다.

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        // @Bean 애노테이션을 통해 이 메소드가 Spring IoC 컨테이너에 의해 관리되는 빈 객체를 생성하는 메소드임을 명시하였습니다.
        // 메소드의 반환형이 BCryptPasswordEncoder임으로, 이 메소드는 BCryptPasswordEncoder 타입의 빈 객체를 생성합니다.

        return new BCryptPasswordEncoder();
        // BCryptPasswordEncoder의 인스턴스를 생성하여 반환합니다. 이 객체는 비밀번호를 암호화하는 데 사용됩니다.
    }
}


// Spring Security의 비밀번호 인코더를 설정하는 클래스입니다. BCryptPasswordEncoder 빈을 등록하여,
// 이 빈이 애플리케이션 전반에 걸쳐 비밀번호 암호화에 사용되게 만듭니다.
// @Bean 애노테이션을 사용하여 passwordEncoder 메소드를 정의함으로써,
// Spring IoC(Inversion of Control) 컨테이너는 이 메소드를 호출하여 반환받는 객체를 관리합니다.