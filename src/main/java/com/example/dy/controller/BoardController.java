//package com.example.dy.controller;
//
//import com.example.dy.entity.Board;
//import com.example.dy.service.BoardService;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.Parameter;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.web.bind.annotation.*;
//
//
//@Tag(name = "게시물", description = "게시물 API")
//@RestController
//@RequestMapping("/api/boards")
//public class BoardController {
//    // BoardService를 주입받아 사용한다.
//    @Autowired
//    private BoardService boardService;
//
//
//
//    @Operation(summary = "게시물 검색",  description ="아이디 번호를 기입하여 검색을합니다.")
//
//    // 모든 게시글을 페이지로 분할하여 검색하는 API를 정의합니다.
//    @GetMapping
//    public Page<Board> getAllBoards(
//            @RequestParam(required = false, defaultValue = "1") @Parameter(description = "게시글 페이지") Integer page,
//            @RequestParam(required = false, defaultValue = "10") @Parameter(description = "몇개 보여줄까?") Integer size
//    ) {
//        Pageable pageable = PageRequest.of(page - 1, size);
//        return boardService.getAllBoards(pageable);
//    }
//
//
//    // ID로 게시글을 조회하는 API
//    @Operation(summary = "게시물 검색",  description ="아이디 번호를 기입하여 검색을합니다.")
//    @GetMapping("/{id}")  // 아이디 검색
//    public Board getBoardById(@PathVariable Integer id) {
//        return boardService.getBoardById(id);
//    }
//
//
//    // 이름으로 게시글을 조회하는 API
//    @GetMapping(params = "name") // 이름 검색
//    public Page<Board> getBoardsByName(@RequestParam(required = false) String name,
//                                       @Parameter(hidden = true) Pageable pageable) {
//        return boardService.getBoardsByName(name, pageable);
//    }
//
//    // 가격으로 게시글을 조회하는 API
//    @GetMapping(params = "job") // 직업 검색
//    public Page<Board> getBoardsByJob(@RequestParam(required = false) String job,
//                                      @Parameter(hidden = true) Pageable pageable) {
//        return boardService.getBoardsByJob(job, pageable);
//    }
//
//
//    // 새로운 게시글을 생성하는 API
//    @Operation(summary = "게시물 생성",  description ="이름과 가격을 게시합니다.")
//    @PostMapping // 게시물 생성
//    public Board createBoard(@RequestBody Board board) {
//        return boardService.createBoard(board);
//    }
//
//    // 게시글을 수정하는 API
//    @Operation(summary = "게시물 수정",  description ="아이디 번호를 기입하여 수정을 합니다.")
//    @PutMapping("/{id}") // 게시물 수정
//    public Board updateBoard(@PathVariable Integer id, @RequestBody Board board) {
//        return boardService.updateBoard(id, board);
//    }
//
//
//    // 게시글을 삭제하는 API
//    @Operation(summary = "게시물 삭제",  description ="아이디 번호를 기입하여 삭제를 합니다.")
//    @DeleteMapping("/{id}")  // 게시물 삭제
//    public void deleteBoard(@PathVariable Integer id) {
//        boardService.deleteBoard(id);
//    }
//}
