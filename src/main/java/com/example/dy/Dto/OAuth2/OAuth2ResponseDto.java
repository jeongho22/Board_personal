package com.example.dy.Dto.OAuth2;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public interface OAuth2ResponseDto {
    //이메일
    String getEmail();
    //사용자 실명 (설정한 이름)
    String getName();

}
