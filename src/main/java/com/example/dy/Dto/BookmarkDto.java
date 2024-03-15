package com.example.dy.Dto;

import com.example.dy.Domain.Bookmark;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;


@ToString
@NoArgsConstructor // 디폴트 생성자 생성
@Getter
@Slf4j
public class BookmarkDto {

    private Long id;
    private Long userId;
    private Long articleId;      // comment 포함된 게시글의 아이디
    private String articleTitle; // 게시글 제목 필드 추가
    private LocalDateTime createdAt;


    // 생성자( 이런식으로 생성 해줘도 되고 @AllArgsConstructor 도 가능)
    public BookmarkDto(Long id,Long articleId,Long userId,String articleTitle,LocalDateTime createdAt) {
        this.id = id;
        this.articleId = articleId;
        this.userId = userId;
        this.articleTitle =articleTitle;
        this.createdAt =createdAt;
    }

    // Dto로 변환
    public static BookmarkDto bookmarkDto(Bookmark bookmark) {
        return new BookmarkDto(
                bookmark.getId(),
                bookmark.getArticle().getId(),
                bookmark.getUser().getId(),
                bookmark.getArticle().getTitle(),
                bookmark.getCreatedAt()
        );
    }


}
