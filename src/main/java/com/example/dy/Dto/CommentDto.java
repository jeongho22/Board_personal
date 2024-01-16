package com.example.dy.Dto;

import com.example.dy.Domain.Comment;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor // 모든 생성자 자동생성
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




    // Comment -> CommentDto로 변환하는 메서드
    public static CommentDto createCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),              // 댓글의 id키 번호
                comment.getArticle().getId(), // 게시글의 id
                comment.getNickname(),
                comment.getBody()

        );
    }
}

