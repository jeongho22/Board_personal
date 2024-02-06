package com.example.dy.Service;
import com.example.dy.Domain.User;
import com.example.dy.Domain.constant.Role;
import com.example.dy.Dto.UserRequestDto;
import com.example.dy.Dto.UserResponseDto;
import com.example.dy.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //생성자 생성 (o) 필드 주입 x
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder; // bean 주입
    }

    // 회원 가입 메서드
    public UserResponseDto register(UserRequestDto registerRequestDto) {
        if (userRepository.findByEmail(registerRequestDto.getEmail()).isPresent()) {
            throw new IllegalStateException("이미 존재 하는 이메일 입니다");
        }
        log.info("1. 회원 가입 과정 requestDto 요청 전 : {}",registerRequestDto);

        //1.회원 가입 할때 요청 되는 3가지를 request 요청을 보냄.
        User user = new User();
        user.setUsername(registerRequestDto.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));
        user.setEmail(registerRequestDto.getEmail());
        user.setRole(Role.USER); // 기본 Role을 USER로 설정

        log.info("Role: {}",user.getRole().name());

        //2.User 안에 dto 로 생성 되어진 객체를 저장
        User saved = userRepository.save(user);

        log.info("2. 회원 가입 과정 requestDto 요청 후: {}",saved);

        //3. ResponseDto 변환
        return UserResponseDto.fromEntity(saved);
    }


    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            return null; //1.null 이거거나 2.인증되지 않았거나 3.anonymousUser인 경우 null 반환
        }

        String email = authentication.getName();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));


        log.info("접속중 아이디: username={}, email={}, role={}, id={}",
                currentUser.getUsername(),
                currentUser.getEmail(),
                currentUser.getRole(),
                currentUser.getId());


        return currentUser;
    }

}