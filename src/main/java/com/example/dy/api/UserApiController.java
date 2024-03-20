
package com.example.dy.api;
import com.example.dy.Dto.Login.UserRequestDto;
import com.example.dy.Dto.Login.UserResponseDto;
import com.example.dy.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
@RestController
public class UserApiController {

    private final UserService userService;
    public UserApiController(UserService userService) {
        this.userService = userService;
    }


    // 1. 유저 정보 등록(POST)
    @PostMapping("/api/users/signup")
    public ResponseEntity<UserResponseDto> register(@Valid @RequestBody UserRequestDto registerRequestDto) {
        try {
            UserResponseDto registerResponseDto = userService.register(registerRequestDto);
            return ResponseEntity.status(HttpStatus.OK).body(registerResponseDto); // 성공 시 UserResponseDto 반환
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage()); // 실패 시 예외 던지기
        }
    }


    // 2.유저 정보(아이디,유저네임) 조회(Get)
    @GetMapping("/api/users")
    public ResponseEntity<Page<UserResponseDto>> getAllUsers(@RequestParam(required = false) String searchType,
                                                             @RequestParam(required = false) String searchKeyword,
                                                             @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable){
        Page<UserResponseDto> users = userService.findAllUsers(searchType,searchKeyword,pageable);
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }


    // 3.유저 중복 조회(Get)
    @GetMapping("/api/users/check-email")
    public ResponseEntity<Boolean> checkEmail(@RequestParam String email) {
        boolean exists = userService.emailExists(email);
        return ResponseEntity.status(HttpStatus.OK).body(exists);
    }



    // 4. 유저 정보 수정(patch)
    @PatchMapping("/api/users/{id}")
    public ResponseEntity<UserResponseDto> update(@PathVariable Long id,
                                                  @RequestBody UserRequestDto dto) { // json 을 받아올때만 dto
        // 서비스에게 위임
        UserResponseDto updatedDto = userService.update(id, dto);
        log.info("4. 댓글 수정 엔티티 -> dto 변환 : {}",updatedDto);
        // 결과 응답
        return ResponseEntity.status(HttpStatus.OK).body(updatedDto);
    }




    // 5. 유저 삭제(delete)
    @DeleteMapping("/api/users/{id}")
    public ResponseEntity<UserResponseDto> delete(@PathVariable Long id){        //ResponseEntity에 ArticleDto을 담아서 보내준다.
        UserResponseDto deleted = userService.delete(id);
        log.info("삭제 변환 성공(Dto) 2: {}",deleted);
        return (deleted!=null) ?
                ResponseEntity.status(HttpStatus.NO_CONTENT).build():
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }


}

