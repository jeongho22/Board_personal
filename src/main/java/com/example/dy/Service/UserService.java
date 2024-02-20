package com.example.dy.Service;
import com.example.dy.Domain.User;
import com.example.dy.Domain.constant.Role;
import com.example.dy.Dto.CustomOAuth2UserDto;
import com.example.dy.Dto.OAuth2ResponseDto;
import com.example.dy.Dto.UserRequestDto;
import com.example.dy.Dto.UserResponseDto;
import com.example.dy.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;


import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //생성자 생성 (o) 필드 주입 x
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 1. 회원 가입 메서드
    public UserResponseDto register(UserRequestDto registerRequestDto) {

        boolean isUser= userRepository.findByEmail(registerRequestDto.getEmail()).isPresent();
        log.info("0. 유저 존재 : {}",isUser); // true false

        if (isUser) {
            throw new IllegalStateException("이미 존재 하는 이메일 입니다.");
        }
        log.info("1. 회원 가입 과정 requestDto 요청 전 : {}",registerRequestDto);

        //1.회원 가입 할때 요청 되는 3가지를 request 요청을 보냄. -> Dto를 -> entity로 변환
        User user = new User();
        user.setUsername(registerRequestDto.getUsername());
        user.setEmail(registerRequestDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));
        user.setRole(Role.USER);        // 기본 Role을 USER로 설정

        //2.User 안에 dto 로 생성 되어진 객체를 저장
        User saved = userRepository.save(user);
        log.info("2. 회원 가입 과정 requestDto 요청 후: {}",saved);

        //3. ResponseDto 변환
        return UserResponseDto.fromEntity(saved);
    }

    // 2. 유저 아이디 조회 서비스
    @Transactional
    public List<UserResponseDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserResponseDto::fromEntity)
                .collect(Collectors.toList());
    }





    //3. 현재 아이디 판별(일반 ,카카오)
    public User getCurrentUser() {

        //세션 정보 확인

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }

        // A instanceof B :
        // 검사할 객체 A
        // 객체가 속할거같은 클래스의 이름 B


            // OAuth2 로그인 처리
        if (authentication.getPrincipal() instanceof CustomOAuth2UserDto) {
            CustomOAuth2UserDto customOAuth2UserDto = (CustomOAuth2UserDto) authentication.getPrincipal();

            log.info(" 1: {}", customOAuth2UserDto);

            String email = customOAuth2UserDto.getEmail();

            log.info(" 2: {}",email);

            User currentUserOAuth2 = userRepository.findByEmail(email)
                    .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));

            log.info("접속중 아이디(카카오): username={}, email={}, role={}, id={}",
                    currentUserOAuth2.getUsername(),
                    currentUserOAuth2.getEmail(),
                    currentUserOAuth2.getRole(),
                    currentUserOAuth2.getId());

            return currentUserOAuth2;


        } else {
            // 일반 로그인 처리
            String email = authentication.getName(); // 일반 로그인 시 이메일

            User currentUser = userRepository.findByEmail(email)
                    .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));

            log.info("세션값 : {}", authentication);

            log.info("일반 이메일 : {} ",email);

            log.info("접속중 아이디(일반): username={}, email={}, role={}, id={}",
                    currentUser.getUsername(),
                    currentUser.getEmail(),
                    currentUser.getRole(),
                    currentUser.getId());

            return currentUser;
        }
    }

}