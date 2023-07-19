package com.example.dy.repository;

// 'repository' 패키지에 속한 인터페이스입니다. 데이터베이스 연산을 담당하는 역할입니다.

import com.example.dy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
// Java의 내장 클래스입니다. 값이 없을 수도 있는 상황을 나타내는 wrapper 클래스입니다.


public interface UserRepository extends JpaRepository<User, Long> {
    // JpaRepository 인터페이스를 상속받아 UserRepository 인터페이스를 정의하였습니다. User 엔티티와 그 primary key의 타입이 Long이라는 것을 지정하였습니다.

    Optional<User> findByUsername(String username);
    // User 테이블에서 username 필드를 기준으로 검색하는 메소드입니다. 반환값은 User 객체가 포함된 Optional입니다.
    // 즉, 해당 username을 가진 User가 있으면 그 User를 반환하고, 없으면 Optional.empty를 반환합니다.

    boolean existsByUsername(String username); // 이 메서드를 추가합니다.

    @Modifying
    @Transactional
    @Query("DELETE FROM User u WHERE u.id IN ?1")
    void deleteAllById(List<Integer> userIds);



}





//User 엔티티에 대한 CRUD 연산을 수행할 수 있는 UserRepository 인터페이스를 정의하고 있습니다.
// Spring Data JPA의 JpaRepository를 상속받아 기본적인 데이터베이스 연산을 지원하며,