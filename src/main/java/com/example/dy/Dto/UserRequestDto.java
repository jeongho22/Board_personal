package com.example.dy.Dto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor


// 회원 가입할때 요청하는 부분.
public class UserRequestDto {
    private String username;
    private String email;
    private String password;
}