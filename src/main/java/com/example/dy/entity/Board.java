package com.example.dy.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data // Lombok에서 제공하는 어노테이션으로, 클래스에 대한 getter, setter, equals, hashCode, toString 메서드를 자동으로 생성해줍니다.
@Entity // JPA에서 제공하는 어노테이션으로, 이 클래스가 엔티티 클래스임을 나타냅니다. 엔티티 클래스란 데이터베이스 테이블과 직접 매핑되는 클래스를 의미합니다.
public class Board {
    @Id // 엔티티의 기본키를 나타내는 어노테이션입니다.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본키의 생성 전략을 지정하는 어노테이션입니다. 여기서는 IDENTITY 전략을 사용하므로, DB가 자동으로 ID를 생성합니다.


    private Integer id;

    @NotBlank(message = "이름이 비었습니다") // 서버 사이드 검증
    @Schema(description = "이름")                                      // 장점 : 보안성, 신뢰성
    private String name;                 // 게시글의 고유 ID

    @Schema(description = "가격")
    @NotBlank(message = "가격이 비었습니다") //  NotNull < NotEmpty <NotBlank는 세 가지 어노테이션 중 가장 강도가 강한 것으로 // null, "", " " 모두 허용하지 않습니다.
    private String job;
    private Integer age;

    @CreationTimestamp
    private LocalDateTime time;
    private Integer views;




    // 한 게시글에는 여러 댓글이 달릴 수 있으므로, 게시글과 댓글은 OneToMany 관계를 갖는다.
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Comment> comments = new ArrayList<>();






   // 1 .mappedBy = "board" : 이 말은 Comment가 있는 '게시글' 공간에 자신의 위치를 알려주는 역할을 한다고 생각하면 됩니다.
   // 예를 들어, 학교에서 친구에게 내 자리를 찾아오라고 할 때, "나는 3학년 2반 7번째 자리에 있어!"라고 알려주는 것과 비슷하답니다.
   // 여기서 '3학년 2반 7번째 자리'가 바로 mappedBy = "board"가 하는 일입니다.


    // 2.cascade = CascadeType.ALL
    // 예를 들어, Board 인스턴스가 삭제되면, 해당 Board에 속한 모든 Comment 인스턴스도 삭제됩니다.


    // 3.fetch = FetchType.LAZY : 이것은 학교에 있는 모든 책을 한 번에 가져오지 않고,
    // 필요할 때마다 가져오는 것과 비슷하다고 생각하면 됩니다. 예를 들어, 수학 시간에는 수학책을,
    // 과학 시간에는 과학책을 가져오는 것처럼요. 이런 방식을 사용하면 불필요하게 많은 책을 한 번에 가져와서 무거운 가방을 들고 다닐 필요가 없습니다.
    // 이렇게 필요한 것만 가져오는 것을 '지연 로딩'이라고 하며, 이는 컴퓨터의 성능을 향상시키는 중요한 방법 중 하나입니다.


    // 총정리


    // mappedBy = "board"는 댓글이 자신이 어떤 게시물에 속해 있는지를 알려주는 역할을 합니다. 이것이 없다면, 각 댓글이 어떤 게시물에 속해 있는지 알 수 없습니다.

    // fetch = FetchType.LAZY는  게시물을 클릭해서 들어갈 때만 그 게시물에 대한 댓글들을 가져오는 것입니다.






}
