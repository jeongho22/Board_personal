package com.example.dy.Domain;

import lombok.*;

import javax.persistence.*;

@Entity                 // DB가 인식 하게끔함..(해당 클래스로 테이블을 만든다)
@AllArgsConstructor     // 생성자를 대신해서 만들어줌...
@ToString
@NoArgsConstructor      // 디폴트 생성자 하나 무조건 만들어줘야함
@Getter                 // @data는 하지만 순환참조나... 각종 로직에 취약함.. getter, setter,  메소드 자동으로 생성
                        // id, title, content 필드에 대한 getId(), getTitle(), getContent() 메소드를 생성
public class Article {

    @Id// 대표값을 하나 넣어줘야함 엔티티에는
    @GeneratedValue(strategy = GenerationType.IDENTITY)// strategy = GenerationType.IDENTITY 이거까지해주면 DB가 ID(1,2,3...)를 자동 생성한다.
    private Long id;

    @Setter @Column private String title;
    @Setter @Column private String content;


//     디폴트 생성자 하나 무조건 만들어줘야함
//    protected Article() {}


//    public Article(Long id,String title,String content) {
//        this.id = id;
//        this.title = title;
//        this.content= content;
//    } // 생성자 하나 만들어주고 ... 이걸 dto에서 변환


        public void patch(Article article) {
        if (article.title != null)
            this.title = article.title;
        if (article.content != null)
            this.content = article.content;
    }

}
