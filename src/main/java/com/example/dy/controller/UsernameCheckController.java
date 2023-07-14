package com.example.dy.controller;

import com.example.dy.entity.User;
import com.example.dy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class UsernameCheckController {
    private UserRepository userRepository;

    @Autowired
    public UsernameCheckController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/check-username")
    public ResponseEntity<?> checkUsername(@RequestParam("username") String username) {
        // 사용자 이름을 검색하고, 결과에 따라 상태 코드를 반환
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            // 사용자가 이미 존재하는 경우, 409 Conflict 상태 코드를 반환
            return new ResponseEntity<>("아이디: 사용할 수 없는 아이디입니다. 다른 아이디를 입력해 주세요.", HttpStatus.CONFLICT);
        } else {
            // 사용자가 없는 경우, 200 OK 상태 코드를 반환
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }
}
