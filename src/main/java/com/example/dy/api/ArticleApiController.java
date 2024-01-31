package com.example.dy.api;


import com.example.dy.Domain.Article;

import com.example.dy.Dto.ArticleDto;
import com.example.dy.Service.ArticleService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
@RestController // RESTAPI 용 컨트롤러 JSON(데이터반환)
public class ArticleApiController {


    @Autowired // DI, 생성 객체를 가져와 연결
    private ArticleService articleService;

    // 1-1.
    // 전체 읽기 Page<Article> 반환
    // List < Page 가 상위 기능 느낌

    @GetMapping("/api/articles")
    public Page<Article> index(@RequestParam(required = false) String searchType,
                                        @RequestParam(required = false) String searchKeyword,
                                        Pageable pageable) {
        return articleService.index(searchType, searchKeyword, pageable);
    }

    // 1-2.단일 게시글 읽기
    @GetMapping("/api/articles/{id}")
    public ResponseEntity<ArticleDto> show(@PathVariable Long id) {
        ArticleDto articleDto = articleService.show(id);
        log.info("단일 조회 변환 이후(Dto) 2 : {}",articleDto);
        return ResponseEntity.ok(articleDto);
    }



    //2. 생성 Post
    @PostMapping("/api/articles")
    public ResponseEntity<ArticleDto> create(@RequestBody ArticleDto dto) { // 1. form 에서 데이터를 던질때는 그냥써도 되지만
        ArticleDto createdDto = articleService.create(dto);                 // 2. restapi 에서 json으로 데이터를 던질때는
        log.info("생성 변환 이후(Dto) 3 : {}",createdDto);                 // 3. @Requestbody 선언해주어야 자바객체 언어로 변환되어진다.
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDto);
    }
    //3. 수정 Patch

    @PatchMapping("/api/articles/{id}")
    public ResponseEntity<ArticleDto> update(@PathVariable Long id, @RequestBody ArticleDto dto) { // 1. 데이터베이스에 무언가 변경될때는 dto 사용해줘야함..
        ArticleDto updatedDto = articleService.update(id, dto);                                    // 2. ResponseEntity 담아서 보내야지만 100,200,300,400 상태코드 보내줄수있음..
        log.info("수정 변환 이후(Dto) 3 : {}",updatedDto);
        return ResponseEntity.ok(updatedDto);
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

// RestController와 controller의 차이는 ?
// controller는 뷰 를 반환하는데
// RestController 는 데이터형태의 json을 반환함

