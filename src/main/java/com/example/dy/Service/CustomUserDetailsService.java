// 로그인 할때 자격 증명

package com.example.dy.Service;

import com.example.dy.Domain.User;
import com.example.dy.Repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    // 생성자 주입 방식
    private final UserRepository userRepository;
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("해당 Email 을 찾을 수 없습니다.: " + email));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), //Authentication 객체 내에서 사용자의 username 사용됌
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name())));

    }
    // 해당 사용자의 정보(이메일, 비밀번호, 권한)를 바탕으로 Spring Security의 UserDetails 구현체를 생성하여 반환합니다.
    // 여기서는 사용자의 권한을 단일 권한 목록으로 변환하기 위해 Collections.singletonList를 사용하고 있습니다.
    // Collections.singletonList 생성된 리스트는 변경할 수 없습니다.
    // Collections.singletonList(User);
    // SimpleGrantedAuthority 객체는 사용자의 권한을 나타내며, 여기서는 사용자의 역할(role)을 권한으로 사용합니다.
}