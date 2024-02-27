package com.example.dy.Dto.OAuth2;

import com.example.dy.Domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class CustomOAuth2UserDto implements OAuth2User {


    private final OAuth2ResponseDto oAuth2ResponseDto;
    private final User user;

    public CustomOAuth2UserDto(OAuth2ResponseDto oAuth2ResponseDto, User user){
        this.oAuth2ResponseDto = oAuth2ResponseDto;
        this.user = user;
    }


    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));

        return collection;
    }

    @Override
    public String getName() {
        return oAuth2ResponseDto.getEmail();
    }




}
