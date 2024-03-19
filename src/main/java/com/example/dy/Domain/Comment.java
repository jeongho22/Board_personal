package com.example.dy.Domain;
import com.example.dy.Dto.CommentDto;
import com.example.dy.Repository.CommentRepository;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor
@SQLDelete(sql = "UPDATE comment SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?") //2.Soft Delete
@Where(clause = "deleted_at IS NULL")
public class Comment extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column @Setter private String nickname;

    @Column @Setter private String body;

    @ManyToOne
    @JoinColumn(name="article_id")                    // 해당 댓글 엔티티 여러개가, 하나의 Article 엔티티에 기본키(PK)와 연결된다.
    private Article article;                          // 즉 더 쉽게 말해서 @JoinColumn (article_id) 는 외래키(FK)이다


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @Setter
    @JoinColumn(name = "parent_id")
    private Comment parentComment;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> childComments = new ArrayList<>();


    @Setter @Column(name = "deleted", nullable = false)
    private boolean deleted = false; // 기본값은 false로 설정


    // 생성자( 이런식으로 생성 해줘도 되고 @AllArgsConstructor 도 가능)
    public Comment(Long id,Article article,String nickname,String body, User user) {
        this.id = id;
        this.article = article;
        this.nickname= nickname;
        this.body = body;
        this.user = user;
    }


    // CommentDto -> Comment로 변환 (대댓글 처리 추가)
    public static Comment createComment(CommentDto dto, Article article, User user, CommentRepository commentRepository) {
        // 기존 조건 검사 코드...
        if (dto.getId() != null)
            throw new IllegalArgumentException("댓글 생성 실패! 댓글의 id가 없어야 합니다."); // 생성할때 댓글의 아이디가 있어야함...
        if (!Objects.equals(dto.getArticleId(), article.getId()))
            throw new IllegalArgumentException("댓글 생성 실패! 게시글의 id가 잘못되었습니다."); // dto.아티클 게시글이 == articleId 다르다면


        Comment parentComment = null;
        if (dto.getParentId() != null) {
            parentComment = commentRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("대댓글 생성 실패! 부모 댓글이 존재하지 않습니다."));

            // 부모 댓글이 이미 다른 댓글의 대댓글인 경우 대댓글 생성 방지
            if (parentComment.getParentComment() != null) {
                throw new IllegalArgumentException("대댓글의 대댓글 생성은 허용되지 않습니다.");
            }

        }
        Comment comment = new Comment(
                null,
                article,
                dto.getNickname(),
                dto.getBody(),
                user
        );
        comment.setParentComment(parentComment); // 부모 댓글 설정
        return comment;
    }


    // 수정 메서드
    public void patch(CommentDto dto) {
        // 예외 발생
        if (!Objects.equals(this.id, dto.getId()))
            throw new IllegalArgumentException("댓글 수정 실패! 잘못된 id가 입력되었습니다.");
        // 객체를 갱신
        if (dto.getNickname() != null)
            this.nickname = dto.getNickname();
        if (dto.getBody() != null)
            this.body = dto.getBody();
    }


}
