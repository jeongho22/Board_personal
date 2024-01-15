package com.example.dy.Dto;

import com.example.dy.Domain.Article;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
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
//    } // @AllArgsConstructor 필드의 생성자 대체 가능
//
//    @Override
//    public String toString() {
//        return "ArticleFormDto{" +
//                "title='" + title + '\'' +
//                ", content='" + content + '\'' +
//                '}';
//
//    }// @ToString 대체 가능
//
//
    public Article toEntity() {                        // ArticleFormDto 객체를 가지고

        return new Article(id, title, content);       // article 엔티티의 생성자들 리턴
    }
}


// 요약하자면, toEntity 메소드는 DTO를 데이터베이스의 엔티티로 변환하는 데 사용되며,
// 이는 데이터 계층과 애플리케이션 계층 사이의 분리를 강화하고, 데이터의 무결성과 보안을 보장하기 위해 필요합니다.


// toEntity 메소드는 ArticleFormDto 객체의 데이터를 사용하여 새로운 Article 엔티티 객체를 생성합니다.
// 이 메소드는 DTO 객체가 가지고 있는 데이터를 엔티티 객체로 변환하는데 사용됩니다.



