package com.example.dy.Service.OAuth2;
import com.example.dy.Domain.User;
import com.example.dy.Domain.constant.Role;
import com.example.dy.Dto.OAuth2.CustomOAuth2UserDto;
import com.example.dy.Dto.OAuth2.GoogleResponseDto;
import com.example.dy.Dto.OAuth2.KaKaoResponseDto;
import com.example.dy.Dto.OAuth2.OAuth2ResponseDto;
import com.example.dy.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;


@Slf4j
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomOAuth2UserService(UserRepository userRepository,PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // kakao나 네이버 이런것들이  OAuth2UserRequest 통해들어옴
        OAuth2User oAuth2User = super.loadUser(userRequest); // oAuth2User 인증 데이터 들이 넘어옴
        log.info("1.OAuth2User: {}",oAuth2User);
        //loadUser 메서드 내에서 OAuth2User 객체로부터 사용자 정보를 추출하여 KaKaoResponseDto 객체에 담는 과정이 이루어집니다.

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        log.info("2.userRequest.getClientRegistration() : {}",userRequest.getClientRegistration());
        log.info("3.userRequest.getClientRegistration().getRegistrationId : {}",registrationId);

        OAuth2ResponseDto oAuth2Responsedto = null; // 카카오, 구글 , 네이버 들어올때마다 초기화 시켜주기위해 null 값으로 지정
        String loginType = null;

        if (registrationId.equals("kakao")) {
            oAuth2Responsedto = new KaKaoResponseDto(oAuth2User.getAttributes()); // 카카오 ,구글, 네이버 다들 바구니가 다름
            loginType = "KAKAO SOCIAL";
        }
        else if (registrationId.equals("google")){
            oAuth2Responsedto = new GoogleResponseDto(oAuth2User.getAttributes());
            loginType = "GOOGLE SOCIAL";
        }
        else{
            return null;
        }

        // 로그 추가 (카카오,네이버,구글 조건에 맞는게 나옴)
        log.info("5.OAuth2User attributes: {}", oAuth2User.getAttributes());

        String email = oAuth2Responsedto.getEmail();
        log.info("6.OAuth2User attributes email: {}", email);
        Optional<User> existData = userRepository.findByEmail(email);

        if (existData.isPresent() && "NORMAL".equals(existData.get().getLoginType())) {
            // 동일한 이메일 주소를 가진 'NORMAL' 로그인 타입의 사용자가 이미 존재하는 경우
            throw new IllegalArgumentException("이미 동일한 이메일 주소로 가입된 '일반' 계정이 있습니다.");
        }


        User user = null;

        if (existData.isEmpty()) {
            user = new User();
            user.setUsername(oAuth2Responsedto.getName());
            user.setEmail(oAuth2Responsedto.getEmail());
            user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
            user.setRole(Role.USER);
            user.setLoginType(loginType); // 로그인 타입을 구체적으로 설정

            userRepository.save(user); // 새 사용자 저장
        } else {

            user = existData.get(); // 기존 사용자 정보 사용
        }

        log.info("7.oAuth2Responsedto: {}",oAuth2Responsedto);


        return new CustomOAuth2UserDto(oAuth2Responsedto, user); // 위에서 받아온 특정 oAuth2Responsedto 같이 넘겨줌
    }

}