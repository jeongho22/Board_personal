package com.example.dy.Domain;

import com.example.dy.Domain.constant.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;



@Getter
@Setter
@NoArgsConstructor  // 기본 생성자 자동 생성
@Entity
public class User extends AuditingFields {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 100, unique = true)
    private String email;

//    @Column(name = "email_verification_token", unique = true)
//    private String emailVerificationToken;
//
//    @Column(name = "email_verified", nullable = false)
//    private boolean emailVerified = false;

    @Enumerated(EnumType.STRING) // enum 이름을 데이터베이스에 문자열로 저장
    @Column(nullable = false)
    private Role role; // Role enum 사용

}