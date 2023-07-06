package com.example.dy.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)


    private Integer id;

    @NotBlank(message = "이름이 비었습니다.") // 서버 사이드 검증
                                          // 장점 : 보안성, 신뢰성
    private String name;
    @NotBlank(message = "가격이 비었습니다.") //  NotNull < NotEmpty <NotBlank는 세 가지 어노테이션 중 가장 강도가 강한 것으로 // null, "", " " 모두 허용하지 않습니다.
    private String job;
    private Integer age;

    @CreationTimestamp
    private LocalDateTime time;
    private Integer views;



    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    //@OneToMany 어노테이션은 Entity 간의 관계를 정의하는 데 사용됩니다.
    //Board 인스턴스가 여러 개의 Comment 인스턴스를 가질 수 있음을 나타냅니다.


   // 1 .mappedBy = "board" : 이 말은 Comment가 있는 '보드'라는 공간에 자신의 위치를 알려주는 역할을 한다고 생각하면 됩니다.
   // 예를 들어, 학교에서 친구에게 내 자리를 찾아오라고 할 때, "나는 3학년 2반 7번째 자리에 있어!"라고 알려주는 것과 비슷하답니다.
   // 여기서 '3학년 2반 7번째 자리'가 바로 mappedBy = "board"가 하는 일입니다.


    // 2.cascade = CascadeType.ALL
    // 예를 들어, Board 인스턴스가 삭제되면, 해당 Board에 속한 모든 Comment 인스턴스도 삭제됩니다.


    // 3.fetch = FetchType.LAZY : 이것은 학교에 있는 모든 책을 한 번에 가져오지 않고,
    // 필요할 때마다 가져오는 것과 비슷하다고 생각하면 됩니다. 예를 들어, 수학 시간에는 수학책을,
    // 과학 시간에는 과학책을 가져오는 것처럼요. 이런 방식을 사용하면 불필요하게 많은 책을 한 번에 가져와서 무거운 가방을 들고 다닐 필요가 없습니다.
    // 이렇게 필요한 것만 가져오는 것을 '지연 로딩'이라고 하며, 이는 컴퓨터의 성능을 향상시키는 중요한 방법 중 하나입니다.


    // 총정리

    // setBoard(Board board) 메서드를 통해 자신이 어떤 게시물에 속해 있는지를 지정합니다.

    // mappedBy = "board"는 댓글이 자신이 어떤 게시물에 속해 있는지를 알려주는 역할을 합니다. 이것이 없다면, 각 댓글이 어떤 게시물에 속해 있는지 알 수 없습니다.

    // fetch = FetchType.LAZY는 이 '자식' 댓글들을 필요할 때만 가져오게 하는 역할을 합니다. 게시물을 클릭해서 들어갈 때만 그 게시물에 대한 댓글들을 가져오는 것입니다.

    // 이렇게 하면 불필요한 데이터를 미리 가져오지 않으므로, 컴퓨터의 성능을 절약할 수 있습니다.





}
