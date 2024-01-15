package com.example.dy.Dto;

import com.example.dy.Domain.Comment;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor // 모든 생성자 자동생성
@ToString
@NoArgsConstructor // 디폴트 생성자 생성
@Getter
public class CommentDto {
    private Long id;            //comment 아이디
    @JsonProperty("article_id") // 낙타 표기법 때문에 언더바 사용을안하고 articleId 으로썼는데 알아서 json 변환해줌
    private Long articleId;    // comment 포함된 게시글의 아이디
    private String nickname;
    private String body;


    // static -> 클래스 메소드 안에 선언해줄때 씀
    public static CommentDto createCommentDto(Comment comment) { // Comment 엔티티를 CommentDto로 변환하는 메서드
        return new CommentDto(
                comment.getId(),              // 댓글의 pk키 번호
                comment.getArticle().getId(), // 게시글의 pk키
                comment.getNickname(),
                comment.getBody()
        );
    }
}


//id	article_id	nickname	body
//1	        10	      Alice	    Nice post!
//2	        10	       Bob	    I agree.



//CommentDto(id=1, articleId=10, nickname="Alice", body="Nice post!")
//CommentDto(id=2, articleId=10, nickname="Bob", body="I agree.")