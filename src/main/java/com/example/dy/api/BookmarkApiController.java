package com.example.dy.api;
import com.example.dy.Domain.User;
import com.example.dy.Dto.BookmarkDto;
import com.example.dy.Service.BookmarkService;
import com.example.dy.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
public class BookmarkApiController {

    private final BookmarkService bookmarkService;
    private final UserService userService; // UserService 추가

    public BookmarkApiController(BookmarkService bookmarkService,UserService userService) {
        this.bookmarkService= bookmarkService;
        this.userService = userService; // UserService 주입
    }



    // 1.사용자 누른 북마크 조회(get)
    @GetMapping("/api/bookmarks")
    public ResponseEntity<List<BookmarkDto>> bookmarksByCurrentUser() {
        // 1-1. 접속자 bookmark 조회
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<BookmarkDto> dtos = bookmarkService.findBookmarksByUserId(currentUser.getId());
        return ResponseEntity.status(HttpStatus.OK).body(dtos);
    }



    // 2.북마크 등록,삭제 (post, delete)
    @PostMapping("/api/bookmarks/toggle/{articleId}")
    public ResponseEntity<Void> toggleBookmark(@PathVariable Long articleId) {

        // 2-1. 접속자 bookmark 조회
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<BookmarkDto> bookmark = bookmarkService.toggleBookmark(articleId, currentUser.getId());
        if (bookmark.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
            // 북마크가 생성되었음
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();  // 북마크가 삭제되었음
        }
    }


}
