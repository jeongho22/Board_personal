package com.example.dy.Repository;

import com.example.dy.Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // 이메일로 사용자 찾기 (이메일 중복 검사에 사용)
    Optional<User> findByEmail(String email);


    // 사용자 이름으로 사용자 찾기 (로그인 인증에 사용)
    Optional<User> findByUsername(String username);
}

// Optional 결과값이 있거나 없을 수 있는 상황을 더 안전하게 처리할 수 있도록 설계