package com.example.dy.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@Entity
    public class Comment {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @NotBlank(message = "이름이 비었습니다.")
        private String author;
        @NotBlank(message = "내용이 비었습니다.")
        private String content;
        private LocalDateTime createdAt;




    @ManyToOne
    // 이것은 Board 엔티티에 대한 다대일 관계를 설정하는데 사용되며,
    // JPA는 기본적으로 대상 엔티티의 이름과 소스 엔티티의 필드 이름을 사용하여 외래키 열의 이름을 생성합니다.

    @JoinColumn(name="board_id")

    //@JoinColumn은 외래키 열의 이름을 명시적으로 지정할 수 있게 해줍니다.
    // 여기서는 board_id라는 이름을 외래키 열의 이름으로 지정하고 있습니다.

    private Board board;

    //FOREIGN KEY (board_id) REFERENCES board(id)
    // borad_id가 board쪽에 id를 참고함...
    // id가 삭제되면 board_id도 삭제

    public void setBoard(Board board) {
        this.board = board;
    }



    // 이 메서드가 없다면, 댓글이 어떤 게시글에 속해야하는지를 변경할 수 없게 돼요.
    // 이를 바꾸려면 댓글을 지우고 다시 써야해요.
    // 이건 마치 잘못된 위치로 가버린 박스를 다시 가져와서 새로운 이름표를 붙여야 하는 것과 같아요.
    // 이는 매우 번거롭고 시간이 많이 들어요.



    // 총정리

    // setBoard(Board board) 메서드를 통해 자신이 어떤 게시물에 속해 있는지를 지정합니다.

    // mappedBy = "board"는 댓글이 자신이 어떤 게시물에 속해 있는지를 알려주는 역할을 합니다. 이것이 없다면, 각 댓글이 어떤 게시물에 속해 있는지 알 수 없습니다.

    // fetch = FetchType.LAZY는 이 '자식' 댓글들을 필요할 때만 가져오게 하는 역할을 합니다. 게시물을 클릭해서 들어갈 때만 그 게시물에 대한 댓글들을 가져오는 것입니다.

    // 이렇게 하면 불필요한 데이터를 미리 가져오지 않으므로, 컴퓨터의 성능을 절약할 수 있습니다.

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    // 시간관리



}
