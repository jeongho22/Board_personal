package com.example.dy.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="user")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank
    @Column(nullable=false, length=50, unique=true)
    private String username;

    @Column(length=100)
    private String password;

    @Column(nullable=false, length=100)
    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Board> boards = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles; // roles 필드 추가

    // getter, setter, 생성자 등

    // UserDetails methods
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 또는 사용자 상태에 따라 반환
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 또는 사용자 상태에 따라 반환
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 또는 사용자 상태에 따라 반환
    }

    @Override
    public boolean isEnabled() {
        return true; // 또는 사용자 상태에 따라 반환
    }




}



// User 클래스는 데이터베이스 테이블에 대응하는 JPA(Entity) 클래스입니다.
// id, username, password, email의 4개 필드가 있으며, 각 필드는 데이터베이스의 컬럼에 해당합니다.
// 각 필드에는 제약조건이 지정되어 있어,
// 예를 들어 username 필드는 중복되지 않아야 하며, null이 될 수 없습니다.