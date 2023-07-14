package com.example.dy.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="user")
public class User {
    // 기본키에 대응하는 식별자 변수
    @Id
    // 1 부터 시작하여 자동으로 1 씩 증가하도록 증가 전략 설정
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;             // 회원 일련번호

    @NotBlank
    @Column(nullable=false, length=50, unique=true)
    private String username;    // 로그인 아이디

    @Column(length=100)
    private String password;
    // 비밀번호
    @Column(nullable=false, length=100)
    private String email;       // 이메일


}



// User 클래스는 데이터베이스 테이블에 대응하는 JPA(Entity) 클래스입니다.
// id, username, password, email의 4개 필드가 있으며, 각 필드는 데이터베이스의 컬럼에 해당합니다.
// 각 필드에는 제약조건이 지정되어 있어,
// 예를 들어 username 필드는 중복되지 않아야 하며, null이 될 수 없습니다.