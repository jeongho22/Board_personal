
package com.example.dy.api;

import com.example.dy.Domain.Article;
import com.example.dy.Dto.UserRequestDto;
import com.example.dy.Dto.UserResponseDto;
import com.example.dy.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
@RestController
public class UserApiController {

    private final UserService userService;
    public UserApiController(UserService userService) {
        this.userService = userService;
    }


    // 1. 유저 정보 등록(POST)

    @PostMapping("/api/signup")
    public ResponseEntity<UserResponseDto> register(@RequestBody UserRequestDto registerRequestDto) {
        try {
            UserResponseDto registerResponseDto = userService.register(registerRequestDto);
            return ResponseEntity.status(HttpStatus.OK).body(registerResponseDto); // 성공 시 UserResponseDto 반환
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage()); // 실패 시 예외 던지기
        }
    }


    // 2.유저 정보 조회(Get)
    @GetMapping("/api/users")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }

}

