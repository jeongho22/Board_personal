package com.example.dy.Repository;

import com.example.dy.Domain.LikeBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeBoardRepository extends JpaRepository<LikeBoard, Long> {


    // 1.게시글 눌린 좋아요 총 수 조회(count)
    @Query("SELECT COUNT(b) FROM LikeBoard b WHERE b.article.id = :articleId")
    Long countByArticleId(@Param("articleId") Long articleId);

    // 2.게시글 눌린 좋아요 상태 확인
    boolean existsByArticleIdAndUserId(Long articleId, Long userId);

    // 3.유저가 누른 좋아요 목록 조회 (소프트 삭제된 게시물 생성 제외)
    @Query("SELECT b FROM LikeBoard b WHERE b.article.deletedAt IS NULL AND b.user.id = :userId")
    List<LikeBoard> findActiveLikeByUserId(@Param("userId") Long userId);

    // 4.좋아요 중복 체크
    Optional<LikeBoard> findByUserIdAndArticleId(Long userId, Long articleId);

    // 5.게시글 관관된 모든 좋아요 조회(게시글 삭제 할때 좋아요 까지 삭제 할려고)
    List<LikeBoard> findByArticleId(Long articleId);

    // 6.유저와 관련된 모든 좋아요 조회 (유저 탈퇴 할때 좋아요 까지 삭제 할려고)
    List<LikeBoard> findByUserId(@Param("UserId") Long UserId);

}
