package com.example.dy.api;


import com.example.dy.Domain.Article;

import com.example.dy.Dto.ArticleFormDto;
import com.example.dy.Repository.ArticleRepository;
import com.example.dy.Service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController // RESTAPI 용 컨트롤러 JSON(데이터반환)
public class ArticleApiController {


    @Autowired // DI, 생성 객체를 가져와 연결
    private ArticleService articleService;

    //Get
    @GetMapping("/api/articles")
    public List<Article> index(){
        return articleService.index();
    }

    @GetMapping("api/articles/{id}")
    public Article show(@PathVariable Long id){
        return articleService.show(id);
    }

    //Post

    @PostMapping("api/articles")
    public ResponseEntity<Article> create(@RequestBody ArticleFormDto dto){ // restapi 에서 json으로 데이터를 받을때 @Requestbody추가
        Article created = articleService.create(dto); // Service에 dto 넘겨줌
        return (created != null) ?
                ResponseEntity.status(HttpStatus.OK).body(created): // null 이아니면 올바른요청
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); //  null 이면 배드리퀘스트
    }

    //Patch

    @PatchMapping("/api/articles/{id}")
    public ResponseEntity<Article> update(@PathVariable Long id ,
                                          @RequestBody ArticleFormDto dto){

        Article updated = articleService.update(id,dto);


        return (updated!=null) ? ResponseEntity.status(HttpStatus.OK).body(updated) :
                                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }



    //Delete

    @DeleteMapping("/api/articles/{id}")
    public ResponseEntity<Article> delete(@PathVariable Long id){ //ResponseEntity에 Article을 담아서 보내준다.
        Article deleted = articleService.delete(id);
        return (deleted!=null) ?
                                ResponseEntity.status(HttpStatus.NO_CONTENT).build():
                                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    // 트랜잭션 -> 실패 -> 롤백!
    @PostMapping("/api/transaction-test")
    public ResponseEntity<List<Article>> transactionTest(@RequestBody List<ArticleFormDto> dtos) {
        List<Article> createdList = articleService.createArticles(dtos);
        return (createdList != null) ?
                ResponseEntity.status(HttpStatus.OK).body(createdList) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

}



// RestController와 controller의 차이는 ?
// controller는 뷰 를 반환하는데
// RestController 는 데이터형태의 json을 반환함