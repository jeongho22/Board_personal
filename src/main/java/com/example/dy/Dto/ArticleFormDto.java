package com.example.dy.Dto;
import com.example.dy.Domain.Article;
import com.example.dy.Domain.Comment;
import lombok.ToString;



@ToString
public class ArticleFormDto {

    private Long id;
    private final String title;
    private final String content;

    // 생성자( 이런식으로 생성 해줘도 되고 @AllArgsConstructor 도 가능)
    public ArticleFormDto(Long id,String title,String content) {
        this.id = id;
        this.title = title;
        this.content= content;

    }


    // Dto - > Entity 변환
    public Article toEntity() {
        return new Article(
                this.id,
                this.title,
                this.content);
    }





}


