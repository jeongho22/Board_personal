package com.example.dy.Domain;


import com.example.dy.Dto.CommentDto;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@ToString
@AllArgsConstructor             // 생성자를 대신해서 만들어줌...
@NoArgsConstructor              // 디폴트 생성자 하나 무조건 만들어줘야함
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name="article_id") private Article article;   // 해당 댓글 엔티티 여러개가, 하나의 Article 엔티티에 기본키(PK)와 연결된다.
                                                                        // 즉 더 쉽게 말해서 @JoinColumn (article_id) 는 외래키(FK)이다


    @Column @Setter private String nickname;


    @Column @Setter private String body;



    // 생성 메서드
    public static Comment createComment(CommentDto dto, Article article) {
        // 예외 발생
        if (dto.getId() != null)
            throw new IllegalArgumentException("댓글 생성 실패! 댓글의 id가 없어야 합니다."); // 생성할때 댓글의 아이디가 있어야함...
        if (dto.getArticleId() != article.getId())
            throw new IllegalArgumentException("댓글 생성 실패! 게시글의 id가 잘못되었습니다."); // 게시글의 id랑 == 댓글의 articleId 다르다면


        // 엔티티 생성 및 반환
        return new Comment(
                dto.getId(),
                article,
                dto.getNickname(),
                dto.getBody()
        );
    }

    // 수정 메서드
    public void patch(CommentDto dto) {
        // 예외 발생
        if (this.id != dto.getId())
            throw new IllegalArgumentException("댓글 수정 실패! 잘못된 id가 입력되었습니다.");
        // 객체를 갱신
        if (dto.getNickname() != null)
            this.nickname = dto.getNickname();
        if (dto.getBody() != null)
            this.body = dto.getBody();
    }
}
