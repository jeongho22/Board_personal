package com.example.dy.serivce;

// User 엔티티 클래스를 임포트합니다.

import com.example.dy.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

// 사용자 인증을 위한 UserPrincipal 클래스입니다. UserDetails 인터페이스를 구현합니다.
public class UserPrincipal implements UserDetails {
    // 인증된 사용자를 표현하는 private User 객체입니다.
    private User user;
    private Collection<? extends GrantedAuthority> authorities;

    // User 객체를 초기화하는 UserPrincipal의 생성자입니다.
    public UserPrincipal(User user, Collection<? extends GrantedAuthority> authorities) {
        this.user = user;
        this.authorities = authorities;
    }

    // UserDetails 인터페이스로부터 오버라이딩된 메소드입니다.
    // 사용자에게 부여된 권한을 반환합니다.
    // 현재는 빈 리스트를 반환하므로 특정 권한이 부여되지 않았음을 의미합니다.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities; // 사용자의 권한에 따라 반환
    }

    // UserDetails 인터페이스로부터 오버라이딩된 메소드입니다.
    // 사용자의 비밀번호를 반환합니다.
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    // UserDetails 인터페이스로부터 오버라이딩된 메소드입니다.
    // 사용자의 이름을 반환합니다.
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    // UserDetails 인터페이스로부터 오버라이딩된 메소드입니다.
    // 사용자 계정이 만료되지 않았음을 확인합니다.
    @Override
    public boolean isAccountNonExpired() {
        return true; // 또는 사용자 상태에 따라 반환
    }

    // UserDetails 인터페이스로부터 오버라이딩된 메소드입니다.
    // 사용자 계정이 잠기지 않았음을 확인합니다.
    @Override
    public boolean isAccountNonLocked() {
        return true; // 또는 사용자 상태에 따라 반환
    }

    // UserDetails 인터페이스로부터 오버라이딩된 메소드입니다.
    // 사용자의 자격 증명이 만료되지 않았음을 확인합니다.
    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 또는 사용자 상태에 따라 반환
    }

    // UserDetails 인터페이스로부터 오버라이딩된 메소드입니다.
    // 사용자가 활성화 상태인지 확인합니다.
    @Override
    public boolean isEnabled() {
        return true; // 또는 사용자 상태에 따라 반환
    }
}