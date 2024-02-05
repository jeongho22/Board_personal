package com.example.dy.Dto;

import com.example.dy.Domain.Comment;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@ToString
@NoArgsConstructor // 디폴트 생성자 생성
@Getter

// comment 담을 그릇
public class CommentDto {
    private Long id;            //comment 아이디
    @JsonProperty("article_id") // 낙타 표기법 때문에 언더바 사용을안하고 articleId 으로썼는데 알아서 json 변환해줌
    private Long articleId;    // comment 포함된 게시글의 아이디
    private String nickname;
    private String body;
    private Long user;


    // 생성자( 이런식으로 생성 해줘도 되고 @AllArgsConstructor 도 가능)
    public CommentDto(Long id,Long articleId,String nickname,String body,Long user) {
        this.id = id;
        this.articleId = articleId;
        this.nickname= nickname;
        this.body = body;
        this.user = user;
    }

    // Comment -> CommentDto로 변환
    public static CommentDto createCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getArticle().getId(),
                comment.getNickname(),
                comment.getBody(),
                comment.getUser().getId()
        );
    }
}

