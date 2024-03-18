package com.example.dy.Dto;
import com.example.dy.Domain.Article;

import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;


@Getter
@ToString
public class ArticleDto {

    private final Long id;
    @NotBlank private final String title;
    @NotBlank private final String content;
    private final Long user;
    private Long view =0L; // 조회수 필드
    private final LocalDateTime createdAt;
    private final String username;
    private final String authorNickname;



    // 생성자
    public ArticleDto(Long id,
                      String title,
                      String content,
                      Long user,
                      Long view,
                      LocalDateTime createdAt,
                      String username,
                      String authorNickname) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.user = user;
        this.view= view;
        this.createdAt = createdAt;
        this.username = username;
        this.authorNickname= authorNickname;

    }

    // Article 엔티티에서 ArticleDto로 변환
    public static ArticleDto fromEntity(Article article) {
        return new ArticleDto(
                article.getId(),
                article.getTitle(),
                article.getContent(),
                article.getUser().getId(),
                article.getView(),
                article.getCreatedAt(),
                article.getUser().getUsername(),
                article.getAuthorNickname()); // Article 엔티티의 viewCount를 Dto의 view로 설정
    }

}


