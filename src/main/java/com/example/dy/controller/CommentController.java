package com.example.dy.controller;

import com.example.dy.dto.CommentDto;
import com.example.dy.entity.Comment;
import com.example.dy.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


@Tag(name = "댓글", description = "댓글 API")  // 댓글과 관련된 API로 분류되는 태그를 추가합니다.
@RestController                              // 이 클래스가 RESTful 웹 서비스 컨트롤러임을 나타냅니다.
@RequestMapping("/api/comments")          // 이 컨트롤러가 처리하는 요청의 공통 경로를 설정합니다.
public class CommentController {



    @Autowired                              // Spring에서 해당 서비스의 의존성을 자동으로 주입하게 합니다.
    private CommentService commentService;



    // 모든 댓글을 페이지로 분할하여 검색하는 API를 정의합니다.

    @Operation(summary = "댓글 검색",  description ="댓글의 글쓴이와 내용을 검색합니다.")
    @GetMapping
    public Page<Comment> getAllComments(
            @RequestParam(required = false, defaultValue = "1") @Parameter(description = "댓글 페이지") Integer page,
            @RequestParam(required = false, defaultValue = "10") @Parameter(description = "몇개 보여줄까?") Integer size) {

        Pageable pageable = PageRequest.of(page - 1, size); // 요청한 페이지와 크기에 따른 페이지 정보를 생성합니다.
        return commentService.getAllComments(pageable);          // 페이지 정보를 바탕으로 모든 댓글을 검색합니다.
    }



    // 댓글 번호를 입력하여 해당 댓글을 검색하는 API를 정의합니다.

    @Operation(summary = "댓글 검색",  description ="댓글 번호를 기입하여 검색을합니다.")
    @GetMapping("/{id}")
    public Comment getCommentById(@PathVariable Integer id) {
        return commentService.getCommentById(id);
    }// 댓글 번호를 바탕으로 댓글을 검색합니다.




    // 댓글의 저자 이름으로 댓글을 검색하는 API를 정의합니다.
    @GetMapping(params = "author")
    public Page<Comment> getCommentsByAuthor(@RequestParam(required = false) String author,
                                             @RequestParam(required = false, defaultValue = "1") Integer page,
                                             @RequestParam(required = false, defaultValue = "10") Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return commentService.getCommentsByAuthor(author, pageable);
    }


    // 댓글 내용으로 댓글을 검색하는 API를 정의합니다.
    @GetMapping(params = "content")
    public Page<Comment> getCommentsByContent(@RequestParam(required = false) String content,
                                              @RequestParam(required = false, defaultValue = "1") Integer page,
                                              @RequestParam(required = false, defaultValue = "10") Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return commentService.getCommentsByContent(content, pageable);
    }


    // 새 댓글을 작성하는 API를 정의합니다.
    @Operation(summary = "댓글 생성",  description ="댓글을 게시 합니다")
    @PostMapping
    public Comment createComment(@RequestBody CommentDto commentDto) {
        return commentService.createComment(commentDto);
    }



    // 댓글을 수정하는 API를 정의합니다.
    @Operation(summary = "댓글 수정",  description ="댓글 번호를 기입하여 수정을 합니다.")
    @PutMapping("/{id}")
    public Comment updateComment(@PathVariable Integer id, @RequestBody Comment comment) {
        return commentService.updateComment(id, comment);
    }




    // 댓글을 삭제하는 API를 정의합니다.
    @Operation(summary = "댓글 삭제",  description ="댓글 번호를 기입하여 삭제를 합니다.")
    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable Integer id) {
        commentService.deleteComment(id);
    }


}
