
package com.example.dy.api;

import com.example.dy.Dto.UserRequestDto;
import com.example.dy.Dto.UserResponseDto;
import com.example.dy.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
@RestController
public class UserApiController {

    private final UserService userService;
    public UserApiController(UserService userService) {
        this.userService = userService;
    }



    @PostMapping("/api/signup")
    public ResponseEntity<?> register(@RequestBody UserRequestDto registerRequestDto) {
        try {
            UserResponseDto registerResponseDto = userService.register(registerRequestDto);
            log.info("회원 가입 성공: {}", registerResponseDto);
            return ResponseEntity.ok(registerResponseDto);
        } catch (IllegalStateException e) {
            log.error("회원 가입 실패: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage())); // 중복된 이메일 에러 메시지 전송
        }
    }


}

