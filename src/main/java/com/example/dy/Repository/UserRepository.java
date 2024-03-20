package com.example.dy.Repository;
import com.example.dy.Domain.Article;
import com.example.dy.Domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    // 1. 전체 조회
    Page<User> findByUsernameContainingOrEmailContaining(String title, String content, Pageable pageable);

    // 2. 제목 조회
    Page<User> findByUsernameContaining(String title, Pageable pageable);

    // 3. 내용 조회
    Page<User> findByEmailContaining(String content, Pageable pageable);

    // 4. 이메일 중복조회
    Optional<User> findByEmail(String email);
    // 해당하는 사용자가 있다면 그 사용자 객체를, 없다면 null을 감싸는 Optional 객체를 반환

}
