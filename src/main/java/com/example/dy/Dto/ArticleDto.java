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



//    // Getter 메서드 추가
//    public Long getId() {
//        return id;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public String getContent() {
//        return content;
//    }



    // 생성자
    public ArticleDto(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    // Article 엔티티에서 ArticleDto로 변환
    public static ArticleDto fromEntity(Article article) {
        return new ArticleDto(
                article.getId(),
                article.getTitle(),
                article.getContent());
    }

}


