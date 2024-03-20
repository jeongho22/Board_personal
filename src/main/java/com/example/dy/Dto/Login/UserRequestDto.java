package com.example.dy.Dto.Login;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor

// 회원 가입할때 요청하는 부분.
public class UserRequestDto {

    @NotBlank
    private String username;

    @Pattern(regexp = "^[a-z0-9]{4,16}$", message = "아이디는 영문 소문자/숫자, 4~16자로만 구성 되어야 합니다.")
    private String email;

    @Size(min = 10, max = 16, message = "비밀번호는 10~16자로 설정 해야 합니다.")
    private String password;

}