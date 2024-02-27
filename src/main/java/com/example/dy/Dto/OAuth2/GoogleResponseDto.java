package com.example.dy.Dto.OAuth2;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;


@Slf4j
public class GoogleResponseDto implements OAuth2ResponseDto {
    private final Map<String,Object> attribute; // 생성자


    public GoogleResponseDto(Map<String,Object> attribute){
        this.attribute = attribute;             // 생성자를 만듬
        log.info("4.GoogleResponseDto 객체 생성됨. attribute 맵에 저장된 데이터: " + attribute); // 객체 생성 로그
    }

    @Override
    public String getEmail() {

        return attribute.get("email").toString();
    }

    @Override
    public String getName() {

        return attribute.get("name").toString();
    }
}
