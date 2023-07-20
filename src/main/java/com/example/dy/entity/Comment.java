package com.example.dy.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data // Lombok에서 제공하는 어노테이션으로, 클래스에 대한 getter, setter, equals, hashCode, toString 메서드를 자동으로 생성해줍니다.
@Entity // JPA에서 제공하는 어노테이션으로, 이 클래스가 엔티티 클래스임을 나타냅니다. 엔티티 클래스란 데이터베이스 테이블과 직접 매핑되는 클래스를 의미합니다.
    public class Comment {
        @Id // 엔티티의 기본키를 나타내는 어노테이션입니다.
        @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본키의 생성 전략을 지정하는 어노테이션입니다. 여기서는 IDENTITY 전략을 사용하므로, DB가 자동으로 ID를 생성합니다.
        private Integer id;

    @Schema(description = "글쓴이")
    @NotBlank(message = "이름이 비었습니다.")

        private String author;

    @Schema(description = "내용")
    @NotBlank(message = "내용이 비었습니다.")
        private String content;
        @CreationTimestamp
        private LocalDateTime createdAt;




    @ManyToOne // 다대일 관계를 정의하는 어노테이션입니다. 여기서는 여러 댓글이 하나의 게시글에 속할 수 있음을 나타냅니다.


    @JoinColumn(name="board_id") // 외래키를 매핑할 때 사용하는 어노테이션입니다. 여기서는 외래키 열의 이름을 "board_id"로 지정합니다.


    @JsonBackReference  // 양방향 연관관계에서 무한 루프에 빠지는 것을 방지하는 어노테이션입니다.

    private Board board;

    //FOREIGN KEY (board_id) REFERENCES board(id)
    // borad_id가 board쪽에 id를 참고함...
    // id가 삭제되면 board_id도 삭제

    public void setBoard(Board board) {
        this.board = board;
    }

    // 이 메서드는 댓글이 어떤 게시글에 속하는지 설정하는 데 사용됩니다. 없으면 댓글이 어떤 게시글에 속하는지 변경할 수 없습니다.


    // 이 메서드가 없다면, 댓글이 어떤 게시글에 속해야하는지를 변경할 수 없게 돼요.
    // 이를 바꾸려면 댓글을 지우고 다시 써야해요.
    // 이건 마치 잘못된 위치로 가버린 박스를 다시 가져와서 새로운 이름표를 붙여야 하는 것과 같아요.
    // 이는 매우 번거롭고 시간이 많이 들어요.





    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    // 시간관리





}
