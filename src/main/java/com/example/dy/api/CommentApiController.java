package com.example.dy.api;



import com.example.dy.Dto.CommentDto;

import com.example.dy.Service.CommentService;
import com.example.dy.annotation.RunningTime;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
@RestController
public class CommentApiController {
    private final CommentService commentService;

    public CommentApiController(CommentService commentService) {
        this.commentService = commentService;

    }

    // 1.게시물에 달린 댓글 갯수 조회(get)
    @GetMapping("/api/articles/count/{articleId}")
    public ResponseEntity<Long> countCommentsByArticleId(@PathVariable Long articleId) {
        Long count = commentService.countCommentsByArticleId(articleId);
        return ResponseEntity.ok(count);
    }



    // 2.댓글 목록 조회(Get)
    @GetMapping("/api/articles/{articleId}/comments")
    public ResponseEntity<List<CommentDto>> comments(@PathVariable Long articleId){

        List<CommentDto> dtos = commentService.comments(articleId); // 위에 입력값 넣음
        return ResponseEntity.status(HttpStatus.OK).body(dtos);     // 200 ok
    }


    // 3. 댓글 생성(post)
    @PostMapping("/api/articles/{articleId}/comments")
    public ResponseEntity<CommentDto> create(@PathVariable Long articleId,
                                             @Valid @RequestBody CommentDto dto) {

        CommentDto createdDto = commentService.create(articleId, dto);
        log.info("댓글 엔티티 -> Dto 변환 : {} ",createdDto);
        return ResponseEntity.status(HttpStatus.OK).body(createdDto);
    }

    // 4. 댓글 수정(patch)
    @PatchMapping("/api/comments/{id}")
    public ResponseEntity<CommentDto> update(@PathVariable Long id,
                                             @Valid @RequestBody CommentDto dto) {

        CommentDto updatedDto = commentService.update(id, dto);
        log.info("댓글 수정 엔티티 -> dto 변환 : {}",updatedDto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedDto);
    }


    // 5. 댓글 삭제 (delete)
    @RunningTime
    @DeleteMapping("/api/comments/{id}")
    public ResponseEntity<CommentDto> delete(@PathVariable Long id) {
        CommentDto deletedDto = commentService.delete(id);
        log.info("댓글 삭제 엔티티 -> dto 변환값 =>{}",deletedDto);
        return ResponseEntity.status(HttpStatus.OK).body(deletedDto);
    }
}
