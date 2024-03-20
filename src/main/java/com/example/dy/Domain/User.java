package com.example.dy.Domain;
import com.example.dy.Domain.constant.Role;

import com.example.dy.Dto.Login.UserRequestDto;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE user SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?") // 소프트 삭제
@Where(clause = "deleted_at IS NULL") //                                        // 삭제되지 않은 엔티티만 조회
public class User extends AuditingFields {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter @Column(nullable = false, length = 50)
    private String username;

    @Setter @Column(nullable = false)
    private String password;

    @Setter @Column(nullable = false, length = 100, unique = true)
    private String email;

    @Enumerated(EnumType.STRING) // enum 이름을 데이터베이스에 문자열로 저장
    @Setter @Column(nullable = false)
    private Role role; // Role enum 사용

    @Setter @Column(nullable = true)
    private String loginType; // 로그인 유형을 저장하는 필드 추가

    @JsonManagedReference // 순환참조 방지(종)
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Article> articles = new ArrayList<>(); // 사용자가 작성한 게시글 목록

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>(); // 사용자가 작성한 댓글 목록

}

