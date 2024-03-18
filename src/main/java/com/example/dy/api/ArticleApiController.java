package com.example.dy.api;



import com.example.dy.Dto.ArticleDto;

import com.example.dy.Service.ArticleService;

import com.example.dy.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
@RestController
public class ArticleApiController {

    private final ArticleService articleService;

    public ArticleApiController(ArticleService articleService) {
        this.articleService = articleService;
    }

    // 1-1.
    // 전체 읽기 Page<Article> 반환
    @GetMapping("/api/articles")
    public ResponseEntity<Page<ArticleDto>> index(@RequestParam(required = false) String searchType,
                               @RequestParam(required = false) String searchKeyword,
                               Pageable pageable) {

        Page<ArticleDto> articlePage = articleService.index(searchType, searchKeyword, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(articlePage);
    }


    // 1-2.단일 게시글 읽기
    @GetMapping("/api/articles/{id}")
    public ResponseEntity<ArticleDto> show(@PathVariable Long id) {
        ArticleDto articleDto = articleService.show(id);
        log.info("단일 조회 변환 이후(Dto) 2 : {}",articleDto);
        return ResponseEntity.status(HttpStatus.OK).body(articleDto);
    }

    //1-3. 인기 게시물
    @GetMapping("/api/articles/popular")
    public ResponseEntity<Page<ArticleDto>> getPopularArticles(
            @RequestParam(required = false) String searchType,
            @RequestParam(required = false) String searchKeyword,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Long likeThreshold = 3L; // 좋아요 기준값
        Long viewCountThreshold = 30L; // 조회수 기준값

        Page<ArticleDto> popularArticles = articleService.findPopularArticles(
                likeThreshold,
                viewCountThreshold,
                searchType,
                searchKeyword,
                pageable);

        return ResponseEntity.status(HttpStatus.OK).body(popularArticles);
    }


    //2. 생성 Post
    @PostMapping("/api/articles")
    public ResponseEntity<ArticleDto> create(@Valid @RequestBody ArticleDto dto) { // 1. form 에서 데이터를 던질때는 그냥써도 되지만
        ArticleDto createdDto = articleService.create(dto);                 // 2. restapi 에서 json으로 데이터를 던질때는
        log.info("생성 변환 이후(Dto) 3 : {}",createdDto);                 // 3. @Requestbody 선언해주어야 자바객체 언어로 변환되어진다.
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDto);
    }

    //3. 수정 Patch
    @PatchMapping("/api/articles/{id}")
    public ResponseEntity<ArticleDto> update(@PathVariable Long id, @Valid @RequestBody ArticleDto dto) { // 1. 데이터베이스에 무언가 변경될때는 dto 사용해줘야함..
        ArticleDto updatedDto = articleService.update(id, dto);                                    // 2. ResponseEntity 담아서 보내야지만 100,200,300,400 상태코드 보내줄수있음..
        log.info("수정 변환 이후(Dto) 3 : {}",updatedDto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedDto);
    }


    //4. 삭제 Delete
    @DeleteMapping("/api/articles/{id}")
    public ResponseEntity<ArticleDto> delete(@PathVariable Long id){                    //ResponseEntity에 ArticleDto을 담아서 보내준다.
        ArticleDto deleted = articleService.delete(id);
        log.info("삭제 변환 성공(Dto) 2: {}",deleted);
        return (deleted!=null) ?
                ResponseEntity.status(HttpStatus.NO_CONTENT).build():
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }



}


