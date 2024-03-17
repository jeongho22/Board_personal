package com.example.dy.Dto;

import com.example.dy.Domain.LikeBoard;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;



@ToString
@NoArgsConstructor // 디폴트 생성자 생성
@Getter
@Slf4j
public class LikeBoardDto {

    private Long id;
    private Long userId;
    private Long articleId;
    private String username;



    // 생성자( 이런식으로 생성 해줘도 되고 @AllArgsConstructor 도 가능)
    public LikeBoardDto(Long id,Long articleId,Long userId,String username) {
        this.id = id;
        this.articleId = articleId;
        this.userId = userId;
        this.username = username;
    }

    // Dto로 변환
    public static LikeBoardDto likeBoardDto(LikeBoard likeBoard) {
        return new LikeBoardDto(
                likeBoard.getId(),
                likeBoard.getArticle().getId(),
                likeBoard.getUser().getId(),
                likeBoard.getUser().getUsername()
        );


    }


}
