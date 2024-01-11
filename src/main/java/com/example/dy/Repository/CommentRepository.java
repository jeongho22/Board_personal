package com.example.dy.Repository;

import com.example.dy.Domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;


@RepositoryRestResource
public interface CommentRepository extends JpaRepository<Comment,Long> {
    // 특정 게시글의 모든 댓글 조회
    @Query(value =
            "SELECT * " +
                    "FROM comment " +
                    "WHERE article_id = :articleId",                    // comment의 article_id 컬럼의 값들과 일치하는값만
            nativeQuery = true)

    List<Comment> findByArticleId(@Param("articleId") Long articleId); // aricleId 를 입력 했을때 Comment의 묶음을 반환 했으면 좋겠다.


    // 특정 닉네임의 모든 댓글 조회
    List<Comment> findByNickname(String nickname);                        // nickname 을 입력 했을때 Comment의 묶음을 반환 했으면 좋겠다



    // 1.@Query를 사용하는 첫번째 방법,
    // 2.META-INF를 만드는 두번째 방법이 있음

}
