package com.example.dy.Repository;
import com.example.dy.Domain.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {


    // 1.유저가 누른 북마크 목록 조회 (소프트 삭제된 게시물 생성 제외)
    @Query("SELECT b FROM Bookmark b WHERE b.article.deletedAt IS NULL AND b.user.id = :userId")
    List<Bookmark> findActiveBookmarksByUserId(@Param("userId") Long userId);

    // 2.북마크 중복 체크
    Optional<Bookmark> findByUserIdAndArticleId(Long userId, Long articleId);

    // 3.게시글 관관된 모든 북마크 조회(게시글 삭제 할때 북마크 까지 삭제 할려고)
    List<Bookmark> findByArticleId(Long articleId);

    // 4.유저와 관련된 모든 북마크 조회 (유저 탈퇴 할때 북마크 까지 삭제 할려고)
    List<Bookmark> findByUserId(@Param("UserId") Long UserId);

}
