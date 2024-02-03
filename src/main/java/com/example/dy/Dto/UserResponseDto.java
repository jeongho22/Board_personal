package com.example.dy.Dto;
import com.example.dy.Domain.User;
import com.example.dy.Domain.constant.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
// 데이터 반환
public class UserResponseDto {
    private Long id;
    private String username;
    private String email;
    private Role role;


    // 생성자( 이런식으로 생성 해줘도 되고 @AllArgsConstructor 도 가능)
    public UserResponseDto(Long id, String username, String email, Role role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
    }

    // Article 엔티티에서 ArticleDto로 변환
    public static UserResponseDto fromEntity(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole());
            // 비밀번호 민감정보만 제외
    }
}