package com.example.dy.Dto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// 회원 가입할때 요청하는 부분.
public class UserRequestDto {
    private String username;
    private String email;
    private String password;


    // 생성자( 이런식으로 생성 해줘도 되고 @AllArgsConstructor 도 가능)
    public UserRequestDto(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password=password;
    }
}