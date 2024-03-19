package com.example.dy.Dto;
import com.example.dy.Domain.Comment;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ToString
@NoArgsConstructor // 디폴트 생성자 생성
@Getter
@Slf4j
// comment 담을 그릇
public class CommentDto {
    private Long id;            //comment 아이디
    @JsonProperty("article_id") // 낙타 표기법 때문에 언더바 사용을안하고 articleId 으로썼는데 알아서 json 변환해줌
    private Long articleId;    // comment 포함된 게시글의 아이디
    private String nickname;
    @NotBlank private String body;
    private Long user;
    private LocalDateTime createdAt;
    private Long parentId; // 대댓글의 경우 부모 댓글의 ID
    // 대댓글 목록을 저장할 필드 추가
    private List<CommentDto> replies = new ArrayList<>(); // 대댓글 목록 추가
    private boolean deleted = false;


    // 생성자( 이런식으로 생성 해줘도 되고 @AllArgsConstructor 도 가능)
    public CommentDto(Long id,Long articleId,String nickname,String body,Long user,LocalDateTime createdAt,Long parentId,boolean deleted) {
        this.id = id;
        this.articleId = articleId;
        this.nickname= nickname;
        this.body = body;
        this.user = user;
        this.createdAt = createdAt;
        this.parentId =parentId;
        this.deleted=deleted;
    }



    // Comment -> CommentDto로 변환 (대댓글 정보 포함)
    public static CommentDto createCommentDto(Comment comment) {
        Long parentId = null;
        if (comment.getParentComment() != null) {
            parentId = comment.getParentComment().getId(); // 부모 댓글 ID 설정
        }
        return new CommentDto(
                comment.getId(),
                comment.getArticle().getId(),
                comment.getNickname(),
                comment.getBody(),
                comment.getUser().getId(),
                comment.getCreatedAt(),
                parentId,
                comment.isDeleted()
                // 대댓글의 경우 부모 댓글의 ID 추가
        );

    }

    // 대댓글을 추가하는 메서드
    public void addReply(CommentDto reply) {
        this.replies.add(reply);
    }
}

