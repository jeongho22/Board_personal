package com.example.dy.api;
import com.example.dy.Domain.User;
import com.example.dy.Dto.LikeBoardDto;
import com.example.dy.Service.LikeBoardService;
import com.example.dy.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@Slf4j
public class LikeBoardApiController {

    private final LikeBoardService likeBoardService;
    private final UserService userService; // UserService 추가

    public LikeBoardApiController(LikeBoardService likeBoardService, UserService userService) {
        this.likeBoardService = likeBoardService;
        this.userService = userService; // UserService 주입
    }


    //1.게시글에 어느 유저 들이 좋아요 를 눌렀는지 확인
    @GetMapping("/api/like/status/{articleId}/user")
    public ResponseEntity<List<LikeBoardDto>> LikeUser(@PathVariable Long articleId) {
        List<LikeBoardDto> dtos = likeBoardService.findLikesByArticleId(articleId);
        return ResponseEntity.ok(dtos);
    };




    //2.게시글 좋아요 횟수,상태 확인
    @GetMapping("/api/like/status/{articleId}")
    public ResponseEntity<Map<String, Object>> LikeStatus(@PathVariable Long articleId) {
        User currentUser = userService.getCurrentUser();
        Map<String, Object> response = new HashMap<>();

        if (currentUser == null) {
            response.put("liked", false);
            response.put("count", 0);
            return ResponseEntity.ok(response);
        }

        // 2-1.좋아요 상태 확인
        boolean isLiked = likeBoardService.isLikedByUser(articleId, currentUser.getId());
        // 2-2.게시글 좋아요 횟수 확인
        Long count = likeBoardService.countLikeByArticleId(articleId);
        response.put("liked", isLiked);
        response.put("count", count);

        return ResponseEntity.ok(response);
    }


    // 3.사용자가 누른 좋아요 목록 조회 기능
    @GetMapping("/api/like")
    public ResponseEntity<List<LikeBoardDto>> LikeByCurrentUser() {
        // 1-1. 접속자 like 조회
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<LikeBoardDto> dtos = likeBoardService.findLikesByUserId(currentUser.getId());
        return ResponseEntity.status(HttpStatus.OK).body(dtos);
    }



    // 4.좋아요 등록,삭제 (post, delete)
    @PostMapping("/api/like/toggle/{articleId}")
    public ResponseEntity<Map<String, Object>> toggleBoardLike(@PathVariable Long articleId) {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<LikeBoardDto> likeboard = likeBoardService.toggleLike(articleId, currentUser.getId());
        Long count = likeBoardService.countLikeByArticleId(articleId);
        Map<String, Object> response = new HashMap<>();
        response.put("count", count);

        if (likeboard.isPresent()) {
            response.put("liked", true);
            return ResponseEntity.ok(response); // 좋아요 생성
        } else {
            response.put("liked", false);
            return ResponseEntity.ok(response); // 좋아요 삭제
        }
    }



}
