package com.example.dy.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank
    @Column(nullable = false, length = 50, unique = true)
    private String username;

    @Column(length = 100)
    private String password;


    @Column(nullable = false, length = 100, unique = true)
    private String email;

    @Column(name = "email_verification_token", unique = true)
    private String emailVerificationToken;


    @Column(name = "email_verified", nullable = false)
    private boolean emailVerified = false;

    public void generateEmailVerificationToken() {
        this.emailVerificationToken = UUID.randomUUID().toString();
    }


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Board> boards = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;

    @Column(name = "approved", nullable = false)
    private boolean approved = false;


}