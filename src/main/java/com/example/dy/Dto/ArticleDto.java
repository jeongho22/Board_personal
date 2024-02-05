package com.example.dy.Dto;
import com.example.dy.Domain.Article;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;



@Getter
@ToString
public class ArticleDto {

    private Long id;
    private final String title;
    private final String content;
    private final Long user;



    // 생성자
    public ArticleDto(Long id, String title, String content,Long user) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.user = user;
    }

    // Article 엔티티에서 ArticleDto로 변환
    public static ArticleDto fromEntity(Article article) {
        return new ArticleDto(
                article.getId(),
                article.getTitle(),
                article.getContent(),
                article.getUser().getId());
    }

}


