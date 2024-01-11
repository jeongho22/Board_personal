package com.example.dy.Dto;

import com.example.dy.Domain.Article;
import lombok.AllArgsConstructor;
import lombok.ToString;


@AllArgsConstructor //    이걸 쓰면 생성자, tostring 이런거 안써도됌...
@ToString
public class ArticleFormDto {

    private Long id;
    private final String title;
    private final String content;

//    public ArticleFormDto(String title , String content) {
//        this.title = title;
//        this.content = content;
//
//    } // 생성자 만들어줌
//
//    @Override
//    public String toString() {
//        return "ArticleFormDto{" +
//                "title='" + title + '\'' +
//                ", content='" + content + '\'' +
//                '}';
//
//    }// ToString
//
//
    public Article toEntity() {                       // ArticleFormDto 객체를 가지고
        return new Article(id, title, content);        //article 엔티티를 새롭게 리턴
    }
}
