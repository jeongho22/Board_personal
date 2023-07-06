package com.example.dy.repository;

import com.example.dy.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    @Modifying

    //어노테이션은 이 메서드가 데이터를 변경하는 쿼리를 실행하겠다는 것을 나타냅니다.
    // 즉, 이 어노테이션이 붙은 메서드는 데이터베이스의 데이터를 수정하는 작업(INSERT, UPDATE, DELETE 등)을 수행합니다.

    @Transactional

    // 트랜잭션 안에서 실행되는 여러 작업은 모두 한 번에 성공하거나 실패합니다.
    // 즉, 작업 중 하나라도 실패하면 모든 작업이 취소(롤백)됩니다.

    @Query("delete from Comment c where c.board.id = :boardId")
    void deleteByBoardId(Integer boardId);


    //@Query 어노테이션은 이 메서드가 실행할 SQL 쿼리를 지정합니다.
    // 이 경우, Comment 엔티티에서 board.id가 주어진 :boardId와 일치하는 모든 엔티티를 삭제하는 쿼리를 실행합니다.
    // :boardId는 메서드의 파라미터로 전달될 값입니다.

}

// 트랜잭션 쿼리이다
// deleteByBoardId 메서드는
// @Modifying과 @Transactional 어노테이션을 사용하여
// 트랜잭션 안에서 데이터를 변경하는 쿼리를 표현합니다.
// 이 메서드는 특정 게시글과 연결된 모든 댓글을 삭제합니다.


// JpaRepository: Spring Data JPA에서 제공하는 인터페이스입니다.
// 이를 상속하면 기본적인 CRUD(Create, Read, Update, Delete) 연산을 SQL 쿼리 없이 처리할 수 있습니다.
// 즉, 데이터베이스와 관련된 코드를 대폭 줄일 수 있습니다.