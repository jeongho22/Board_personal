package com.example.dy.Repository;

import com.example.dy.Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // 이메일로 사용자 찾기 (이메일 중복 검사에 사용)
    Optional<User> findByEmail(String email);
}

// 해당하는 사용자가 있다면 그 사용자 객체를, 없다면 null을 감싸는 Optional 객체를 반환