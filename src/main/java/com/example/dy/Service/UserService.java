package com.example.dy.Service;
import com.example.dy.Domain.User;
import com.example.dy.Domain.constant.Role;
import com.example.dy.Dto.UserRequestDto;
import com.example.dy.Dto.UserResponseDto;
import com.example.dy.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponseDto register(UserRequestDto requestDto) {
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new IllegalStateException("Email already in use");
        }
        log.info("1. 회원가입 과정 requestDto 요청 전 : {}",requestDto);

        User user = new User();
        user.setUsername(requestDto.getUsername());
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        user.setEmail(requestDto.getEmail());
        user.setRole(Role.USER); // 기본 Role을 USER로 설정

        User saved = userRepository.save(user);

        log.info("2. 회원가입 과정 requestDto 요청 후: {}",saved);

        return UserResponseDto.fromEntity(saved);
    }



}