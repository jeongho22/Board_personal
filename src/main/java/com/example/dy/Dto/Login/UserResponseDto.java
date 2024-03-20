package com.example.dy.Dto.Login;
import com.example.dy.Domain.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
// 데이터 반환
public class UserResponseDto {
    private Long id;
    private String username;
    private String email;
    private LocalDateTime created_at;
    private String loginType;


    // 생성자( 이런식으로 생성 해줘도 되고 @AllArgsConstructor 도 가능)
    public UserResponseDto(Long id,String username, String email,LocalDateTime created_at,String loginType) {
        this.id =id ;
        this.username = username;
        this.email = email;
        this.created_at = created_at;
        this.loginType =loginType;
    }

    // user 엔티티에서 UserResponseDto로 변환
    public static UserResponseDto fromEntity(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getLoginType());
            // 비밀번호 민감정보만 제외
    }
}
