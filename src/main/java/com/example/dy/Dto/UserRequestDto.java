package com.example.dy.Dto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

//입력처리
public class UserRequestDto {
    private String username;
    private String email;
    private String password;
    // 생성자, getter, setter는 Lombok이 처리합니다.
}