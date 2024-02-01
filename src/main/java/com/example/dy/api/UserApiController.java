
package com.example.dy.api;

import com.example.dy.Dto.UserRequestDto;
import com.example.dy.Dto.UserResponseDto;
import com.example.dy.Service.UserService;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@AllArgsConstructor
public class UserApiController {

    private final UserService userService;

    @PostMapping("/api/signup")
    public ResponseEntity<UserResponseDto> register(@RequestBody UserRequestDto requestDto) {
        UserResponseDto userResponseDto = userService.register(requestDto);

        log.info("3. 회원가입 과정 userResponseDto 변환: {}",userResponseDto);

        return ResponseEntity.ok(userResponseDto);
    }

}
