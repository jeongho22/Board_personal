package com.example.dy.Domain;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;



@Entity                 // DB가 인식 = 매핑
@ToString
@NoArgsConstructor      // JPA ENTITY 기본 생성자 생성(필수)            protected Article() {}
@Getter
public class Article extends AuditingFields {

    @Id                                                 // entity 대표값 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY)// strategy = GenerationType.IDENTITY 이거까지해주면 DB가 ID(1,2,3...)를 자동 생성한다.
    private Long id;

    @Setter @Column private String title;   // setter 쓰면 아래 patch 함수 안써도됌 , 근데 캡슐화 약화되어짐
    @Setter @Column private String content; // setter 쓰면 아래 patch 함수 안써도됌 , 근데 캡슐화 약화되어짐

    @Column(columnDefinition = "integer default 0", nullable = false) private long view; // view 값은 엔티티가 아니라 repository에서 갱신

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 댓글을 작성한 사용자



    // 생성자
    public Article(Long id,String title,String content,User user) {
        this.id = id;
        this.title = title;
        this.content= content;
        this.user = user; // 사용자 설정
    }



    // 수정 메서드
    public void patch(Article article) {
        if (article.title != null)
            this.title = article.title;
        if (article.content != null)
            this.content = article.content;
    }


}

