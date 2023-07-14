// CommentRepository.java
package com.example.dy.repository;

import com.example.dy.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository // Spring Framework에 이 인터페이스가 Data Access Object (DAO) 레이어에 속하는 Bean임을 알립니다.
public interface CommentRepository extends JpaRepository<Comment, Integer> { // Comment 엔티티의 CRUD 작업을 위한 Spring Data JPA 리포지토리를 정의합니다.
                                                                             // 두 번째 매개변수는 엔티티의 ID 타입입니다.

    // 작성자 이름에 특정 문자열이 포함된 댓글을 검색하는 메서드를 선언합니다.
    // Spring Data JPA가 메서드 이름을 분석하여 해당하는 쿼리를 자동으로 생성합니다.
    // 반환 타입인 Page는 페이지네이션 정보와 함께 조회 결과를 담습니다.
    Page<Comment> findByAuthorContaining(String author, Pageable pageable);


    // 내용에 특정 문자열이 포함된 댓글을 검색하는 메서드를 선언합니다.
    // Spring Data JPA가 메서드 이름을 분석하여 해당하는 쿼리를 자동으로 생성합니다.
    // 반환 타입인 Page는 페이지네이션 정보와 함께 조회 결과를 담습니다.
    Page<Comment> findByContentContaining(String content, Pageable pageable);

}