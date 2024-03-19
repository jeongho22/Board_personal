package com.example.dy.Repository;
import com.example.dy.Domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {


    // 1.특정 게시글에 달린 댓글 총 수 조회(count)
    @Query("SELECT COUNT(b) FROM Comment b WHERE b.article.id = :articleId")
    Long countByArticleId(@Param("articleId") Long articleId);


    // 2.특정 게시글의 모든 댓글 조회
    @Query(value =
                    "SELECT * " +
                    "FROM comment " +
                    "WHERE article_id = :articleId",                    // comment의 article_id 컬럼의 값들과 일치하는값만
                    nativeQuery = true)
    List<Comment> findByArticleId(@Param("articleId") Long articleId);


    // 3. Soft Delete  = 게시글 ID(articleId)에 해당하며 아직 삭제되지 않은(즉, deletedAt 필드가 null인) 댓글만을 조회
    List<Comment> findByArticleIdAndDeletedAtIsNull(@Param("articleId") Long articleId);

    // 4. 특정 댓글에 모든 유저 아이디 조회
    List<Comment> findByUserId(@Param("UserId") Long UserId);

    // 5. 부모 댓글 ID를 기준으로 대댓글 조회
    List<Comment> findByParentComment(Comment parentComment);

}
