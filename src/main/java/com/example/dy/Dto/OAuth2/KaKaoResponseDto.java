package com.example.dy.Dto.OAuth2;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;


@Slf4j
public class KaKaoResponseDto implements OAuth2ResponseDto {

    private final Map<String,Object> attribute; // 생성자


    public KaKaoResponseDto(Map<String,Object> attribute){
        this.attribute = attribute;             // 생성자를 만듬
        log.info("4.KaKaoResponseDto 객체 생성됨. attribute 맵에 저장된 데이터: " + attribute); // 객체 생성 로그
    }


    @Override
    public String getEmail() {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attribute.get("kakao_account");
        return kakaoAccount.get("email").toString(); // 이메일을 추출합니다.
    }

    @Override
    public String getName() {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attribute.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
        return profile.get("nickname").toString(); // 프로필에서 닉네임(이름)을 추출합니다.
    }
}