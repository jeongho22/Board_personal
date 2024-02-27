package com.example.dy.Dto.Login;

import com.example.dy.Domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomUserDetailsDto implements UserDetails {


    // 생성자 만듬
    private final User user;
    public CustomUserDetailsDto(User user){
        this.user = user;
    }


    // 이메일, 패스워드 ,권한 세개를 반환해줌... 하지만 권한은 null 처리해도 로그인이 되지만, 로그인을 하고 특정 권한게시물은 못들어감
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        //0. 권한 리스트 하나 만듬
        List<GrantedAuthority> authorities = new ArrayList<>();

        //1. 사용자 역할을 기반으로 권한 추가
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())) ;

        //2. SimpleGrantedAuthority 객체를 생성.
        //   SimpleGrantedAuthority는 Security에서 권한을 나타내는 기본 구현체 중 하나로, 단일 문자열 값을 권한갖는다.

        //3. 필요한 경우 추가 권한을 여기에 추가할 수 있습니다.
        // 예: authorities.add(new SimpleGrantedAuthority("ANOTHER_ROLE"));

        return authorities; // 변경 가능한 권한 리스트 반환
    }


    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
