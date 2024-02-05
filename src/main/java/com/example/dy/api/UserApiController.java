
package com.example.dy.api;

import com.example.dy.Dto.UserRequestDto;
import com.example.dy.Dto.UserResponseDto;
import com.example.dy.Service.ArticleService;
import com.example.dy.Service.UserService;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
@RestController
public class UserApiController {

    private final UserService userService;
    public UserApiController(UserService userService) {
        this.userService = userService;
    }



    @PostMapping("/api/signup")
    public ResponseEntity<UserResponseDto> UserApiControllerRegister(@RequestBody UserRequestDto registerRequestDto) { // UserRequestDto 데이터(JSON) -> 객체 변환
        UserResponseDto resisterResponseDto = userService.register(registerRequestDto);

        log.info("3. 회원 가입 과정 userResponseDto 변환: {}",resisterResponseDto);

        return ResponseEntity.ok(resisterResponseDto);
    }

}

// 클라이언트가 보낸 요청의 본문을 UserRequestDto 타입의 객체로 변환하여 requestDto 변수에 저장
// JSON 데이터를 UserRequestDto 객체로 자동 변환합니
// .
