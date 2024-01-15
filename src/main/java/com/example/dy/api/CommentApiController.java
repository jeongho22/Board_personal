package com.example.dy.api;


import com.example.dy.Dto.CommentDto;
import com.example.dy.Service.CommentService;
import com.example.dy.annotation.RunningTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RestController
public class CommentApiController {

    @Autowired
    private CommentService commentService;


    // 댓글 목록 조회
    @GetMapping("/api/articles/{articleId}/comments")
    public ResponseEntity<List<CommentDto>> comments(@PathVariable Long articleId){ //ResponseEntity는 HTTP 응답에 대한 전체 메시지를 나타냅니다.

        // 1.서비스에게 위임
        // 2.코멘트를 바로 반환하기 보다는 dto로 만들어서 반환
        List<CommentDto> dtos = commentService.comments(articleId); // 위에 입력값 넣음

        return ResponseEntity.status(HttpStatus.OK).body(dtos);     // 무조건 옳은 응답으로 일단해줌
    }

    // 댓글 생성
    @PostMapping("/api/articles/{articleId}/comments")
    public ResponseEntity<CommentDto> create(@PathVariable Long articleId,
                                             @RequestBody CommentDto dto) {
        // 서비스에게 위임
        CommentDto createdDto = commentService.create(articleId, dto);
        log.info("댓글 생성 엔티티 -> dto 변환값 =>{}",createdDto);
        // 결과 응답
        return ResponseEntity.status(HttpStatus.OK).body(createdDto);
    }

    // 댓글 수정
    @PatchMapping("/api/comments/{id}")
    public ResponseEntity<CommentDto> update(@PathVariable Long id,
                                             @RequestBody CommentDto dto) { // json 을 받아올때만 dto
        // 서비스에게 위임
        CommentDto updatedDto = commentService.update(id, dto);
        log.info("댓글 수정 엔티티 -> dto 변환값 =>{}",updatedDto);
        // 결과 응답
        return ResponseEntity.status(HttpStatus.OK).body(updatedDto);
    }


    // 댓글 삭제
    @RunningTime
    @DeleteMapping("/api/comments/{id}")
    public ResponseEntity<CommentDto> delete(@PathVariable Long id) {
        // 서비스에게 위임
        CommentDto deletedDto = commentService.delete(id);
        log.info("댓글 삭제 엔티티 -> dto 변환값 =>{}",deletedDto);
        // 결과 응답
        return ResponseEntity.status(HttpStatus.OK).body(deletedDto);
    }
}
